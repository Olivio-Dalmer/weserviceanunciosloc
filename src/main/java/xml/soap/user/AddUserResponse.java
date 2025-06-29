package xml.soap.user;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "AddUserResponse", namespace = "http://user.soap.xml")
public class AddUserResponse {

    @XmlElement(name = "mensagem", namespace = "http://user.soap.xml")
    private String mensagem;

    @XmlElement(name = "status", namespace = "http://user.soap.xml")
    private boolean status;

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
