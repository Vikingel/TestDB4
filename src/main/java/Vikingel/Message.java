package Vikingel;

public class Message {
    int id;
    String mes;
    int sender_id;
    int target_id;


    public Message(int id, String mes, int sender_id, int target_id) {
        this.id = id;
        this.mes = mes;
        this.sender_id = sender_id;
        this.target_id = target_id;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", mes='" + mes + '\'' +
                ", sender_id=" + sender_id +
                ", target_id=" + target_id +
                '}';
    }
}
