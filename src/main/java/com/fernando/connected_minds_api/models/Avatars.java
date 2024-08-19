package com.fernando.connected_minds_api.models;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class Avatars {
    private List<Avatar> males;
    private List<Avatar> females;
}