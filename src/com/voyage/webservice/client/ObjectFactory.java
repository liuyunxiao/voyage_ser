
package com.voyage.webservice.client;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.voyage.webservice.client package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _RtBuyGoods_QNAME = new QName("http://impl.webservice.voyage.com/", "rtBuyGoods");
    private final static QName _RtBuyGoodsResponse_QNAME = new QName("http://impl.webservice.voyage.com/", "rtBuyGoodsResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.voyage.webservice.client
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link RtBuyGoods }
     * 
     */
    public RtBuyGoods createRtBuyGoods() {
        return new RtBuyGoods();
    }

    /**
     * Create an instance of {@link RtBuyGoodsResponse }
     * 
     */
    public RtBuyGoodsResponse createRtBuyGoodsResponse() {
        return new RtBuyGoodsResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RtBuyGoods }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://impl.webservice.voyage.com/", name = "rtBuyGoods")
    public JAXBElement<RtBuyGoods> createRtBuyGoods(RtBuyGoods value) {
        return new JAXBElement<RtBuyGoods>(_RtBuyGoods_QNAME, RtBuyGoods.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RtBuyGoodsResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://impl.webservice.voyage.com/", name = "rtBuyGoodsResponse")
    public JAXBElement<RtBuyGoodsResponse> createRtBuyGoodsResponse(RtBuyGoodsResponse value) {
        return new JAXBElement<RtBuyGoodsResponse>(_RtBuyGoodsResponse_QNAME, RtBuyGoodsResponse.class, null, value);
    }

}
