import java.nio.charset.StandardCharsets;

public class Packet {
    private String data;

    public Packet(String data){
        this.data = data;
    }

    public void setData(String data){
        this.data = data;
    }

    public String getData(){
        return data;
    }

    public void print(){
        System.out.println(data);
    }

    public boolean hasData(){
        return data != "";
    }
}
