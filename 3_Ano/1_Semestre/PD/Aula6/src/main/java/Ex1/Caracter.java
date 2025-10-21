package Ex1;

import java.util.List;

//pode-se realizar com um record
class Caracter {
    public String getFullName() {
        return fullName;
    }

    public String getHogwartsHouse() {
        return hogwartsHouse;
    }

    public String getInterpretedBy() {
        return interpretedBy;
    }

    public List<String> getChildren() {
        return children;
    }

    public String getNickname() {
        return nickname;
    }

    public String getImage() {
        return image;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public int getIndex() {
        return index;
    }

    //children tem de ser lista
    protected List<String> children;
    protected String fullName, nickname, hogwartsHouse, interpretedBy, image, birthdate;
    protected int index;

    @Override
    public String toString() {
        return "Caracter{" +
                "fullName='" + fullName + System.lineSeparator() +
                ", nickname='" + nickname + System.lineSeparator() +
                ", hogwartsHouse='" + hogwartsHouse + System.lineSeparator() +
                ", interpretedBy='" + interpretedBy + System.lineSeparator() +
                ", children='" + children + System.lineSeparator() +
                ", image='" + image + System.lineSeparator() +
                ", birthdate='" + birthdate + System.lineSeparator() +
                '}';
    }
    //nao precisamos de um construtor para a class
    //private Caracter(){}
}
