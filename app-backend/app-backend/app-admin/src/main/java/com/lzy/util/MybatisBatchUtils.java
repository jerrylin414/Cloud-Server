package com.lzy.util;

import com.lzy.service.impl.AppFolderServiceImpl;
import lombok.NonNull;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.List;
import java.util.function.BiFunction;

/**
 * @auther jerry
 * @date 17/7/2024 2:45 PM
 *
 * 暂时用不到这个类，如果遇到大数据再用
 */
@Component
public class MybatisBatchUtils {
    private static Logger logger = LogManager.getLogger(MybatisBatchUtils.class);

    @NonNull
    private SqlSessionFactory sqlSessionFactory;

    @NonNull
    private DataSourceTransactionManager manager;

    public MybatisBatchUtils(@NonNull SqlSessionFactory sqlSessionFactory, @NonNull DataSourceTransactionManager manager) {
        this.sqlSessionFactory = sqlSessionFactory;
        this.manager = manager;
    }

    private DefaultTransactionDefinition def = new DefaultTransactionDefinition();

    {
        // 非只读模式
        def.setReadOnly(false);
        //事务隔离级别 采用数据库默认的
        def.setIsolationLevel(DefaultTransactionDefinition.ISOLATION_DEFAULT);
        //事务传播行为 - required  支持当前事务，如果当前没有事务，就新建一个事务。调用这个方式时，如果外层方法上加了@Transactional注解，事务由外层方法控制。否则
        def.setPropagationBehavior(DefaultTransactionDefinition.PROPAGATION_REQUIRED);
    }

    /**
     * 一次性插入最大 行数
     */
    private final int BATCH_COUNT = 300;

    public <T, U, R> int batchUpdateOrInsert(List<T> data, Class<U> mapperClass, BiFunction<List<T>, U, R> function) {
        if (data.size() == 0) {
            logger.warn("批插入的数据为空！");
            return 0;
        }
        TransactionStatus status = manager.getTransaction(def);
        int batchCount = BATCH_COUNT;
        SqlSession batchSqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
        try {
            U mapper = batchSqlSession.getMapper(mapperClass);
            int size = data.size();
            // 每批最后一个的下标
            int batchLastIndex = batchCount;

            for (int index = 0; index < size; ) {

                if (batchLastIndex >= size) {
                    //一波流
                    batchLastIndex = size;
                    //执行批处理
                    function.apply(data.subList(index, batchLastIndex), mapper);
                    batchSqlSession.commit();
                    batchSqlSession.clearCache();
                    logger.info("batchInsertWpRealOverdueDetail", "index:" + index + " batchLastIndex:" + batchLastIndex);
                    // 数据插入完毕，退出循环
                    break;
                } else {
                    // 多插几次 BATCH_COUNT
                    function.apply(data.subList(index, batchLastIndex), mapper);
                    batchSqlSession.commit();
                    batchSqlSession.clearCache();
                    logger.info( "batchInsertWpRealOverdueDetail", "index:" + index + " batchLastIndex:" + batchLastIndex);
                    // 设置下一批下标
                    index = batchLastIndex;
                    batchLastIndex = index + batchCount;
                }
            }
            manager.commit(status);
        } catch (Exception e) {
            logger.error("批处理失败！",e);
            manager.rollback(status);
            throw e;
        } finally {
            batchSqlSession.close();

        }
        return data.size();
    }
}
