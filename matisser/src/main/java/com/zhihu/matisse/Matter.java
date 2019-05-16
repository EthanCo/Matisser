package com.zhihu.matisse;

/**
 * Matter
 *
 * @author Heiko
 * @date 2019/5/15
 */
public class Matter {
    private String type;
    private int position;
    private String request;

    public Matter(int position, String request) {
        this.position = position;
        this.request = request;
    }

    public Matter(int position, String request, String type) {
        this.position = position;
        this.request = request;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }
}
