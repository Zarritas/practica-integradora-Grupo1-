package org.grupo1.tienda.controller;

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.grupo1.tienda.config.MongoConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/producto")
public class RestControllerMongo {
    @Autowired
    MongoCollection<Document> conexionMongo = MongoConfig.conectarMongo();

    @GetMapping("/listado")
    public List<Document> obtenerProductos() {
        List<Document> productos = new ArrayList<>();
        try (MongoCursor<Document> cursor = conexionMongo.find().iterator()) {
            while (cursor.hasNext()) {
                Document producto = cursor.next();
                productos.add(producto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return productos;
    }

    @PostMapping("/crear")
    public void crearProducto(@RequestParam Map<String,String> todosLosParametros) {
        Document data = new Document();
        for (Map.Entry<String,String> parametros : todosLosParametros.entrySet()) {
            switch (parametros.getKey()) {
                case "_id" :
                    data.append("_id", parametros.getValue());
                    break;
                case "image":
                    String imagen = "http://localhost:8080/images/"+parametros.getValue();
//                    String imagen = "http://172.19.0.3:8080/images/"+parametros.getValue();
                    data.append(parametros.getKey(), imagen);
                    break;
                default:
                    data.append(parametros.getKey(), parametros.getValue());
                    break;
            }
        }
        conexionMongo.insertOne(data);
    }
    @PostMapping("/actualizar/{id}")
    public void actualizarProducto(@PathVariable int id, @RequestParam Map<String,String> todosLosParametros) {
        List<Bson> actualizaciones = new ArrayList<>();
        for (Map.Entry<String,String> parametros : todosLosParametros.entrySet()) {
            switch (parametros.getKey()) {
                case "cantidad" -> actualizaciones.add(Updates.set(parametros.getKey(),Integer.parseInt(parametros.getValue())));
                case "image" -> actualizaciones.add(Updates.set(parametros.getKey(),"http://localhost:8080/images/"+parametros.getValue()));
                default -> actualizaciones.add(Updates.set(parametros.getKey(), parametros.getValue()));
            }
        }
        conexionMongo.updateOne(Filters.eq("_id",id),actualizaciones,new UpdateOptions().upsert(true));
    }

    @DeleteMapping("/borrar-por-id/{id}")
    public void borrarProducto(@PathVariable Long id) {
        conexionMongo.deleteOne(Filters.eq("_id",id));
    }

    @DeleteMapping("/borrar-todo")
    public void borrarProductos() {
        conexionMongo.deleteMany(Filters.exists("_id"));
    }
}