package com.lzy.test;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.UUID;
import com.github.pagehelper.PageHelper;
import com.lzy.common.ViewContentTypeEnum;
import com.lzy.dao.AppFilesMapper;
import com.lzy.dao.AppFolderMapper;
import com.lzy.email.EmailService;
import com.lzy.entity.AppFolder;
import com.lzy.entity.AppFolderExample;

import com.lzy.oss.template.MinioTemplate;
import com.lzy.service.AppFolderService;
import com.lzy.vo.AppFolderVo;
import io.minio.GetObjectArgs;
import io.minio.GetObjectResponse;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.*;
import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class DemoTest {
    private static Logger logger = LogManager.getLogger(DemoTest.class);


    @Autowired
    private AppFolderMapper appFolderMapper;

    @Autowired
    private AppFilesMapper appFilesMapper;

    @Autowired
    private AppFolderService appFolderService;

    @Autowired
    private MinioTemplate minioTemplate;

    @Resource
    private MinioClient client;

    @Test
    public void test(){
        // 假设你有一个Date对象
        Date date = new Date(); // 这里只是为了示例，你可以使用任何有效的Date对象

        // 将Date转换为LocalDate
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        // 增加一天
        LocalDate oneDayLater = localDate.plusDays(1);
        System.out.println("One day later: " + oneDayLater);

        // 增加一个月
        LocalDate oneMonthLater = localDate.plusMonths(1);
        System.out.println("One month later: " + oneMonthLater);

        // 增加二十天
        LocalDate twentyDaysLater = localDate.plusDays(20);
        System.out.println("Twenty days later: " + twentyDaysLater);

        // 假设的到期日期
        LocalDate expiryDate = LocalDate.now().plusMonths(1); // 例如，一个月后到期

        // 判断是否到期
        if (twentyDaysLater.isAfter(expiryDate) || twentyDaysLater.isEqual(expiryDate)) {
            System.out.println("Date is expired or will expire soon.");
        } else {
            System.out.println("Date is not expired.");
        }
        System.out.println(22);
    }

    @Test
    public void downloads() throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        GetObjectResponse is = null;
        GetObjectArgs getObjectArgs =
                GetObjectArgs.builder().bucket("image-bucket").object("/20240715/test_1721031639158.png")
                        .build();
        is = client.getObject(getObjectArgs);
        try {
            is.transferTo(new FileOutputStream("C:\\studyPlace\\123.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("2222");
    }

    @Test
    public void test01() throws InterruptedException {
        AppFolderExample exa = new AppFolderExample();
        exa.createCriteria().andUserIdEqualTo(1l);
        List<AppFolder> list = appFolderMapper.selectByExample(exa);

        //组装成树形结构
        //找到所有的一级分类(也就是树的根节点)
        List<AppFolder> ikuns = list.stream().filter(ikun ->
                ikun.getParentId() == 0
        ).map((menu) -> {
            menu.setChildren(getChildrens(menu,list));
            return menu;
        }).collect(Collectors.toList());

        System.out.println("kuns:" + ikuns);

        Thread.sleep(2000000);
    }

    private List<AppFolder> getChildrens(AppFolder root,List<AppFolder> all){
        List<AppFolder> children = all.stream().filter(ikun -> {
            return ikun.getParentId() == root.getId();
        }).map(ikun  -> { //找子部门
            ikun.setChildren(getChildrens(ikun ,all));
            return ikun;
        }).collect(Collectors.toList());
	return children;
}

    @SneakyThrows
    @Test
    public void testUpload(){
        String imgPath = "C:\\studyPlace\\vue_study\\git-app\\app-backend\\app-backend\\app-admin\\src\\main\\resources\\images\\7aa207caddd84a12939cd6c7c608ec17.png";
        FileInputStream inputStream = new FileInputStream(imgPath);

        MinioClient client = MinioClient.builder().credentials("admin", "minioadmin").endpoint("http://127.0.0.1:9000").build();

        PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                .object("test.png")
                .contentType(ViewContentTypeEnum.getContentType("test.png"))
                .bucket("image-bucket")
                .stream(inputStream, inputStream.available(), -1).build();
        client.putObject(putObjectArgs);

        System.out.println("success-----");
    }

    @Test
    public void teste(){
        PageHelper.startPage(1,5);
        AppFolderExample exa = new AppFolderExample();
        exa.createCriteria().andUserIdEqualTo(1l).andParentIdEqualTo(0l);

        List<AppFolder> folderList = appFolderMapper.selectByExample(exa);
        System.out.println("folderList = " + folderList);
    }


    @Test
    public void testup() throws InterruptedException {
        AppFolder appFolder = new AppFolder();
        appFolder.setName("folderName");
        appFolder.setUserId(1l);
        appFolder.setParentId(1l);
        appFolder.setIsDeleted("N");
        appFolder.setIsBin("N");
        appFolder.setCreateTime(new Date());
        int insertId = appFolderMapper.insert(appFolder);
        logger.error("insertId======" + insertId);
        logger.error("appFolderId======" + appFolder.getId());
        Thread.sleep(5000); // 暂停5秒
    }

    @Test
    public void testTree() throws InvocationTargetException, IllegalAccessException {
        AppFolderExample example = new AppFolderExample();
        example.createCriteria().andUserIdEqualTo(1l);
        List<AppFolder> all = appFolderMapper.selectByExample(example);

        List<AppFolder> list = new ArrayList<>();
        List<AppFolderVo> voList = new ArrayList<>();
        for (AppFolder appFolder : all) {
            AppFolderVo vo = new AppFolderVo();
            BeanUtil.copyProperties(appFolder,vo);
            list.add(appFolder);
            voList.add(vo);
        }
        buildeDepetTree(voList);
    }

    private List<AppFolderVo> buildeDepetTree(List<AppFolderVo> depts){
        //存放构建好的部门树
        ArrayList<AppFolderVo> deptTrees = new ArrayList<>();

        //存放顶级父部门
        ArrayList<AppFolderVo> parents = new ArrayList<>();

        //寻找顶级父部门 可以优化成stream流的写法
        for (AppFolderVo dept : depts) {
            AppFolderVo vo = buildeParent(dept, depts);
            if (!parents.contains(vo)){
                parents.add(vo);
            }
        }

        for (AppFolderVo parent : parents) {
            AppFolderVo appFolderVo = buildTree(parent, depts);
            deptTrees.add(appFolderVo);
        }

        return deptTrees;
    }

    private AppFolderVo buildTree(AppFolderVo parent, List<AppFolderVo> depts) {
        ArrayList<AppFolderVo> childrens = new ArrayList<>();
        for (AppFolderVo vo : depts) {
            if (parent.getId().equals(vo.getParentId())){
                childrens.add(buildTree(vo,depts));
            }
        }
        parent.setChildrenFolders(childrens);
        return parent;
    }

    //
    private AppFolderVo buildeParent(AppFolderVo dept, List<AppFolderVo> depts) {
        AppFolderVo parent = dept;
        for (AppFolderVo folderVo : depts) {
            if (dept.getParentId().equals(folderVo.getId())){
                parent = buildeParent(folderVo,depts);
            }
        }
        return parent;
    }

    @Autowired
    private EmailService emailService;

    @Test
    public void testEmail(){
        emailService.sendGmailSimple("jerrylin414@gmail.com","hgnezzqfvjjoyutc","912256979@qq.com","111","111");
        System.out.println("1");

    }


    @Test
    @Transactional
    public void testGet(){
        List<Long> ids = getAllChildFolderIds(2l);
        //2 6 5 4
        AppFolder folder = null;


        try {
            List<Long> list = appFilesMapper.idListByFolderId(ids);
//            appFolderMapper.batchToBin(ids);
//            Long id = folder.getId();//异常  测试是否回滚
            System.out.println("--");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("6");
        }

    }


    public List<Long> getAllChildFolderIds(Long folderId) {
        List<Long> result = new ArrayList<>();

        AppFolder folder = appFolderMapper.selectByPrimaryKey(folderId);
        if (folder == null) {
            return result;
        }
        result.add(folderId);

        // 递归地查找所有子文件夹的ID
        AppFolderExample example = new AppFolderExample();
        example.createCriteria().andParentIdEqualTo(folderId);
        List<AppFolder> list = appFolderMapper.selectByExample(example);//所有该目录的子文件夹

        if (list == null){
            return result;
        }else {
            for (AppFolder app : list) {
                result.addAll(getAllChildFolderIds(app.getId()));
            }
        }
        return result;
    }


}
