package shop.catchmind.picture.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PictureType {
    HOUSE("house"),
    TREE("tree"),
    MALE("male"),
    FEMALE("female");

    private final String type;
}
