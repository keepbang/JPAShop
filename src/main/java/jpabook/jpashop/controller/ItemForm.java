package jpabook.jpashop.controller;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ItemForm {
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    private String itemType;

    private String author;
    private String isbn;

    private String artist;
    private String etc;

    private String director;
    private String actor;
}
