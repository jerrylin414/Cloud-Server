package com.lzy.dao;

import com.lzy.entity.AppShare;
import com.lzy.entity.AppShareExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AppShareMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table app_share
     *
     * @mbg.generated Thu Jul 25 12:34:13 HKT 2024
     */
    long countByExample(AppShareExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table app_share
     *
     * @mbg.generated Thu Jul 25 12:34:13 HKT 2024
     */
    int deleteByExample(AppShareExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table app_share
     *
     * @mbg.generated Thu Jul 25 12:34:13 HKT 2024
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table app_share
     *
     * @mbg.generated Thu Jul 25 12:34:13 HKT 2024
     */
    int insert(AppShare row);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table app_share
     *
     * @mbg.generated Thu Jul 25 12:34:13 HKT 2024
     */
    int insertSelective(AppShare row);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table app_share
     *
     * @mbg.generated Thu Jul 25 12:34:13 HKT 2024
     */
    List<AppShare> selectByExample(AppShareExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table app_share
     *
     * @mbg.generated Thu Jul 25 12:34:13 HKT 2024
     */
    AppShare selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table app_share
     *
     * @mbg.generated Thu Jul 25 12:34:13 HKT 2024
     */
    int updateByExampleSelective(@Param("row") AppShare row, @Param("example") AppShareExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table app_share
     *
     * @mbg.generated Thu Jul 25 12:34:13 HKT 2024
     */
    int updateByExample(@Param("row") AppShare row, @Param("example") AppShareExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table app_share
     *
     * @mbg.generated Thu Jul 25 12:34:13 HKT 2024
     */
    int updateByPrimaryKeySelective(AppShare row);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table app_share
     *
     * @mbg.generated Thu Jul 25 12:34:13 HKT 2024
     */
    int updateByPrimaryKey(AppShare row);
}