package dev.colibri.githubclienttest.entity;

public class Owner {

    private String login;
    private int id;
    private String avatarUrl;

    public Owner(String login, int id, String avatarUrl) {
        this.login = login;
        this.id = id;
        this.avatarUrl = avatarUrl;
    }

    public String getLogin() {
        return login;
    }

    public int getId() {
        return id;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final Owner owner = (Owner) o;

        if (id != owner.id) return false;
        if (login != null ? !login.equals(owner.login) : owner.login != null) return false;
        return avatarUrl != null ? avatarUrl.equals(owner.avatarUrl) : owner.avatarUrl == null;
    }

    @Override
    public int hashCode() {
        int result = login != null ? login.hashCode() : 0;
        result = 31 * result + id;
        result = 31 * result + (avatarUrl != null ? avatarUrl.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Owner{" + "login='" + login + '\'' + ", id=" + id + ", avatarUrl='" + avatarUrl + '\'' + '}';
    }
}