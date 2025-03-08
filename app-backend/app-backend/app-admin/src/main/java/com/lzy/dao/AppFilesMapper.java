package com.lzy.dao;

import com.lzy.entity.AppFiles;
import com.lzy.entity.AppFilesExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AppFilesMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table app_files
     *
     * @mbg.generated Thu Jul 18 12:45:29 HKT 2024
     */
    long countByExample(AppFilesExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table app_files
     *
     * @mbg.generated Thu Jul 18 12:45:29 HKT 2024
     */
    int deleteByExample(AppFilesExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table app_files
     *
     * @mbg.generated Thu Jul 18 12:45:29 HKT 2024
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table app_files
     *
     * @mbg.generated Thu Jul 18 12:45:29 HKT 2024
     */
    int insert(AppFiles row);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table app_files
     *
     * @mbg.generated Thu Jul 18 12:45:29 HKT 2024
     */
    int insertSelective(AppFiles row);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table app_files
     *
     * @mbg.generated Thu Jul 18 12:45:29 HKT 2024
     */
    List<AppFiles> selectByExample(AppFilesExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table app_files
     *
     * @mbg.generated Thu Jul 18 12:45:29 HKT 2024
     */
    AppFiles selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table app_files
     *
     * @mbg.generated Thu Jul 18 12:45:29 HKT 2024
     */
    int updateByExampleSelective(@Param("row") AppFiles row, @Param("example") AppFilesExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table app_files
     *
     * @mbg.generated Thu Jul 18 12:45:29 HKT 2024
     */
    int updateByExample(@Param("row") AppFiles row, @Param("example") AppFilesExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table app_files
     *
     * @mbg.generated Thu Jul 18 12:45:29 HKT 2024
     */
    int updateByPrimaryKeySelective(AppFiles row);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table app_files
     *
     * @mbg.generated Thu Jul 18 12:45:29 HKT 2024
     */
    int updateByPrimaryKey(AppFiles row);

    int batchToBin(@Param("idList") List<Long> idList);

    List<Long> idListByFolderId(@Param("folderIdList") List<Long> folderIdList);
}