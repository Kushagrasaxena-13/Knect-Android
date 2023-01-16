package com.example.login.Model;

public class Post {

     String postid;
     String postimage;
     String description;
     String publisher;

    public Post() {
    }

    public Post(String postid, String post_image, String description, String publisher) {
        this.postid = postid;
        this.postimage = post_image;
        this.description = description;
        this.publisher = publisher;
    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    public String getPostimage() {
        return postimage;
    }

    public void setPostimage(String postimage) {
        this.postimage = postimage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
}
