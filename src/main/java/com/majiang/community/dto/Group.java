package com.majiang.community.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Author wxh
 * 2023/4/8 21:36
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Group {

    private String groupName;

    private List<TagDTO> tagGroup;
}
