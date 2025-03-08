package com.lzy.bo;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
/**
 * @auther jerry
 * @date 3/9/2024 5:40 PM
 */
@Data
@ToString
@Accessors(chain = true)
public class InitTaskParam {

    /**
     * 文件唯一标识(MD5)
     */
    @NotBlank(message = "文件标识不能为空")
    private String identifier;

    /**
     * 文件大小（byte）
     */
    @NotNull(message = "文件大小不能为空")
    private Long totalSize;

    /**
     * 分片大小（byte）
     */
    @NotNull(message = "分片大小不能为空")
    private Long chunkSize;

    /**
     * 文件名称
     */
    @NotBlank(message = "文件名称不能为空")
    private String fileName;

    @NotNull(message = "文件夹id不能为空")
    private Long folderId;

    @NotNull(message = "角色id不能为空")
    private Long userId;
}
