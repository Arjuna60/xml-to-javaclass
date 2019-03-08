package com.vedatech.pro.controller;

import cfdi.Comprobante;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.*;

@Controller
@RequestMapping("/api/customer")
public class InvoiceController {
    HttpHeaders headers = new HttpHeaders();

    //-------------------Received Xml Customer File--------------------------------------------------------
    @RequestMapping(value = "/send-xml-file", consumes = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> getXmlInvoice(@RequestBody String comprobante) throws IOException {
        HttpStatus status = HttpStatus.OK;
        //Save the uploaded file to this folder
        System.out.println("Comprobante " + comprobante.toString());
        StringReader com = new StringReader(comprobante);
        JAXBContext context = null;
        try {

            context = JAXBContext.newInstance(Comprobante.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            Comprobante unmarshal = (Comprobante) unmarshaller.unmarshal(com);
            System.out.println(unmarshal.getEmisor().getRfc());
            System.out.println(unmarshal.getEmisor().getNombre());
            System.out.println(unmarshal.getReceptor().getNombre());
           System.out.println("FACTURA INTERFACTURA ID " +  unmarshal.getAddenda().getFacturaInterfactura().getId());
            System.out.println("FACTURA INTERFACTURA TIPO DOCUMENTO " +  unmarshal.getAddenda().getFacturaInterfactura().getTipoDocumento());
            System.out.println("FACTURA INTERFACTURA EMISOR PROVEEDOR" +  unmarshal.getAddenda().getFacturaInterfactura().getEmisor().getNumProveedor());
            System.out.println("FACTURA INTERFACTURA EMISOR PROVEEDOR" +  unmarshal.getAddenda().getFacturaInterfactura().getEncabezado().getNumSucursal());
                List<Comprobante.Addenda.FacturaInterfactura.Cuerpo> cuerpos = unmarshal.getAddenda().getFacturaInterfactura().getEncabezado().getCuerpos();

                            for (Comprobante.Addenda.FacturaInterfactura.Cuerpo c: cuerpos) {
                                System.out.println("RENGLON " + c.getRenglon() + " " + c.getCantidad() + " " + c.getEAN13() +
                                        " " + c.getConcepto() + " " + c.getPUnitario() + " " + c.getSubtotal());
                            }


            System.out.println(unmarshal.getSubTotal());
            System.out.println(unmarshal.getTotal());
            Comprobante.Addenda addenda = unmarshal.getAddenda();
            List<Object> list = addenda.getAny();

            String findRfc = unmarshal.getReceptor().getRfc();
            System.out.println("RFC " + findRfc);


            return new ResponseEntity<String>(headers, status);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<String>(headers, status);

    }
}
