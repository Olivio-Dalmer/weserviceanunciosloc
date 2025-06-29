package com.AnunciosLoc.AnunciosLoc.config.web;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

@EnableWs
@Configuration
public class WebServiceConfig extends WsConfigurerAdapter {

   @Bean
    ServletRegistrationBean<MessageDispatcherServlet> messageDispatcherServlet(ApplicationContext contexto) {
        MessageDispatcherServlet servlet = new MessageDispatcherServlet();
        servlet.setApplicationContext(contexto);
        servlet.setTransformWsdlLocations(true);
        return new ServletRegistrationBean<MessageDispatcherServlet>(servlet, "/ws/*");
    }

  @Bean(name = "user")
    DefaultWsdl11Definition userWsdlDefinition ( XsdSchema  userSchema ) {
        DefaultWsdl11Definition wsdl11def = new DefaultWsdl11Definition();

        wsdl11def.setPortTypeName("/apiSoapHttpBikeSharedUser");
        wsdl11def.setLocationUri("/ws");
        wsdl11def.setTargetNamespace("http://user.soap.xml");
        wsdl11def.setSchema(userSchema);

        return wsdl11def;
    }

    @Bean
    XsdSchema userSchema () {
        return new SimpleXsdSchema(new ClassPathResource("xsd/user_schema.xsd") );
    }

    @Bean(name = "anuncios")
    DefaultWsdl11Definition anuncioWsdlDefinition ( XsdSchema  anunciosSchema ) {
        DefaultWsdl11Definition wsdl11def = new DefaultWsdl11Definition();

        wsdl11def.setPortTypeName("/apiSoapHttpBikeSharedAnuncio");
        wsdl11def.setLocationUri("/ws");
        wsdl11def.setTargetNamespace("http://anuncios.soap.xml");
        wsdl11def.setSchema(anunciosSchema);

        return wsdl11def;
    }

   @Bean
    XsdSchema anunciosSchema () {
        return new SimpleXsdSchema(new ClassPathResource("xsd/anuncios_schema.xsd") );
    }

    // --- Local ---
    @Bean(name = "local")
    DefaultWsdl11Definition localWsdlDefinition(XsdSchema localSchema) {
        DefaultWsdl11Definition wsdl = new DefaultWsdl11Definition();
        wsdl.setPortTypeName("ApiSoapHttpBikeSharedLocal");
        wsdl.setLocationUri("/ws");
        wsdl.setTargetNamespace("http://local.soap.xml");
        wsdl.setSchema(localSchema);
        return wsdl;
    }

    @Bean
    XsdSchema localSchema() {
        return new SimpleXsdSchema(new ClassPathResource("xsd/local_schema.xsd"));
    }

        // --- interesses ---
    @Bean(name = "interesses")
    DefaultWsdl11Definition interessesWsdlDefinition(XsdSchema interessesSchema) {
        DefaultWsdl11Definition wsdl = new DefaultWsdl11Definition();
        wsdl.setPortTypeName("ApiSoapHttpBikeSharedinteresses");
        wsdl.setLocationUri("/ws");
        wsdl.setTargetNamespace("http://interesses.soap.xml");
        wsdl.setSchema(interessesSchema);
        return wsdl;
    }

    @Bean
    XsdSchema interessesSchema() {
        return new SimpleXsdSchema(new ClassPathResource("xsd/interesses_schema.xsd"));
    }

    // --- Station ---
    @Bean(name = "station")
    DefaultWsdl11Definition estacaoWsdlDefinition (XsdSchema stationSchema) {
        DefaultWsdl11Definition wsdl11def = new DefaultWsdl11Definition();

        wsdl11def.setPortTypeName("/apiSoapHttpBikeSharedEstacao");
        wsdl11def.setLocationUri("/ws");
        wsdl11def.setTargetNamespace("http://station.soap.xml");
        wsdl11def.setSchema(stationSchema);

        return wsdl11def;
    }

    @Bean
    XsdSchema stationSchema() {
        return new SimpleXsdSchema(new ClassPathResource("xsd/station_schema.xsd"));
    }

    @Bean(name = "replication")
    DefaultWsdl11Definition replicationWsdlDefinition (XsdSchema replicationSchema) {
        DefaultWsdl11Definition wsdl11def = new DefaultWsdl11Definition();

        wsdl11def.setPortTypeName("/apiSoapHttpBikeSharedReplication");
        wsdl11def.setLocationUri("/ws");
        wsdl11def.setTargetNamespace("http://replication.xml");
        wsdl11def.setSchema(replicationSchema);

        return wsdl11def;
    }

    @Bean
    XsdSchema replicationSchema() {
        return new SimpleXsdSchema(new ClassPathResource("xsd/replication.xsd"));
    }

}








