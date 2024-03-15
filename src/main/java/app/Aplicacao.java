package app;

import static spark.Spark.*;
import service.AnimacaoService;

public class Aplicacao {
	
	private static AnimacaoService animacaoService = new AnimacaoService();
	
    public static void main(String[] args) {
        port(6789);
        
        staticFiles.location("/public");
        
        post("/animacao/insert", (request, response) -> animacaoService.insert(request, response));

        get("/animacao/:id", (request, response) -> animacaoService.get(request, response));
        
        get("/animacao/list/:orderby", (request, response) -> animacaoService.getAll(request, response));

        get("/animacao/update/:id", (request, response) -> animacaoService.getToUpdate(request, response));
        
        post("/animacao/update/:id", (request, response) -> animacaoService.update(request, response));
           
        get("/animacao/delete/:id", (request, response) -> animacaoService.delete(request, response));
    }
}
