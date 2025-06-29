//
// Este arquivo foi gerado pela Arquitetura JavaTM para Implementação de Referência (JAXB) de Bind XML, v2.2.11 
// Consulte <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Todas as modificações neste arquivo serão perdidas após a recompilação do esquema de origem. 
// Gerado em: 2025.06.11 às 01:13:46 PM WAT 
//


package xml.soap.anuncios;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java de anonymous complex type.
 * 
 * <p>O seguinte fragmento do esquema especifica o conteúdo esperado contido dentro desta classe.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="mensagem" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="estado" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="usuario" type="{http://anuncios.soap.xml}UserType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="LocalType" type="{http://anuncios.soap.xml}LocalType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="anuncios" type="{http://anuncios.soap.xml}AnuncioType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "mensagem",
    "estado",
    "usuario",
    "localType",
    "anuncios"
})
@XmlRootElement(name = "GetAnuncioResponse")
public class GetAnuncioResponse {

    @XmlElement(required = true)
    protected String mensagem;
    protected boolean estado;
    protected List<UserType> usuario;
    @XmlElement(name = "LocalType")
    protected List<LocalType> localType;
    protected List<AnuncioType> anuncios;

    /**
     * Obtém o valor da propriedade mensagem.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMensagem() {
        return mensagem;
    }

    /**
     * Define o valor da propriedade mensagem.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMensagem(String value) {
        this.mensagem = value;
    }

    /**
     * Obtém o valor da propriedade estado.
     * 
     */
    public boolean isEstado() {
        return estado;
    }

    /**
     * Define o valor da propriedade estado.
     * 
     */
    public void setEstado(boolean value) {
        this.estado = value;
    }

    /**
     * Gets the value of the usuario property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the usuario property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getUsuario().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link UserType }
     * 
     * 
     */
    public List<UserType> getUsuario() {
        if (usuario == null) {
            usuario = new ArrayList<UserType>();
        }
        return this.usuario;
    }

    /**
     * Gets the value of the localType property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the localType property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLocalType().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link LocalType }
     * 
     * 
     */
    public List<LocalType> getLocalType() {
        if (localType == null) {
            localType = new ArrayList<LocalType>();
        }
        return this.localType;
    }

    /**
     * Gets the value of the anuncios property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the anuncios property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAnuncios().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AnuncioType }
     * 
     * 
     */
    public List<AnuncioType> getAnuncios() {
        if (anuncios == null) {
            anuncios = new ArrayList<AnuncioType>();
        }
        return this.anuncios;
    }

}
