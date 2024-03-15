package service;

import java.util.Scanner;
import java.time.LocalDate;
import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import dao.AnimacaoDAO;
import model.Animacao;
import spark.Request;
import spark.Response;

public class AnimacaoService {

    private AnimacaoDAO animacaoDAO = new AnimacaoDAO();
    private String form;
    private final int FORM_INSERT = 1;
    private final int FORM_DETAIL = 2;
    private final int FORM_UPDATE = 3;
    private final int FORM_ORDERBY_ID = 1;
    private final int FORM_ORDERBY_NOME = 2;
    private final int FORM_ORDERBY_DURACAO = 3;

    public AnimacaoService() {
        makeForm();
    }

    public void makeForm() {
        makeForm(FORM_INSERT, new Animacao(), FORM_ORDERBY_NOME);
    }

    public void makeForm(int orderBy) {
        makeForm(FORM_INSERT, new Animacao(), orderBy);
    }

    public void makeForm(int tipo, Animacao animacao, int orderBy) {
        String nomeArquivo = "form.html";
        form = "";
        try {
            Scanner entrada = new Scanner(new File(nomeArquivo));
            while (entrada.hasNext()) {
                form += (entrada.nextLine() + "\n");
            }
            entrada.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        String umAnimacao = "";
        if (tipo != FORM_INSERT) {
            umAnimacao += "\t<table width=\"80%\" bgcolor=\"#f3f3f3\" align=\"center\">";
            umAnimacao += "\t\t<tr>";
            umAnimacao += "\t\t\t<td align=\"left\"><font size=\"+2\"><b>&nbsp;&nbsp;&nbsp;<a href=\"/animacao/list/1\">Novo Animacao</a></b></font></td>";
            umAnimacao += "\t\t</tr>";
            umAnimacao += "\t</table>";
            umAnimacao += "\t<br>";
        }

        if (tipo == FORM_INSERT || tipo == FORM_UPDATE) {
            String action = "/animacao/";
            String nome, genero, buttonLabel;
            if (tipo == FORM_INSERT) {
                action += "insert";
                nome = "Inserir Animacao";
                genero = "Ação, Drama, ...";
                buttonLabel = "Inserir";
            } else {
                action += "update/" + animacao.getId();
                nome = "Atualizar Animacao (ID " + animacao.getId() + ")";
                genero = animacao.getGenero();
                buttonLabel = "Atualizar";
            }
            umAnimacao += "\t<form class=\"form--register\" action=\"" + action + "\" method=\"post\" id=\"form-add\">";
            umAnimacao += "\t<table width=\"80%\" bgcolor=\"#f3f3f3\" align=\"center\">";
            umAnimacao += "\t\t<tr>";
            umAnimacao += "\t\t\t<td colspan=\"3\" align=\"left\"><font size=\"+2\"><b>&nbsp;&nbsp;&nbsp;" + nome + "</b></font></td>";
            umAnimacao += "\t\t</tr>";
            umAnimacao += "\t\t<tr>";
            umAnimacao += "\t\t\t<td colspan=\"3\" align=\"left\">&nbsp;</td>";
            umAnimacao += "\t\t</tr>";
            umAnimacao += "\t\t<tr>";
            umAnimacao += "\t\t\t<td>&nbsp;Nome: <input class=\"input--register\" type=\"text\" name=\"nome\" value=\"" + nome + "\"></td>";
            umAnimacao += "\t\t\t<td>Duração: <input class=\"input--register\" type=\"text\" name=\"duracao\" value=\"" + animacao.getDuracao() + "\"></td>";
            umAnimacao += "\t\t\t<td>Gênero: <input class=\"input--register\" type=\"text\" name=\"genero\" value=\"" + genero + "\"></td>";
            umAnimacao += "\t\t</tr>";
            umAnimacao += "\t\t<tr>";
            umAnimacao += "\t\t\t<td>&nbsp;Data de fabricação: <input class=\"input--register\" type=\"text\" name=\"dataFabricacao\" value=\"" + animacao.getDataFabricacao().toString() + "\"></td>";
            umAnimacao += "\t\t\t<td>Data de lançamento: <input class=\"input--register\" type=\"text\" name=\"dataLancamento\" value=\"" + animacao.getDataLancamento().toString() + "\"></td>";
            umAnimacao += "\t\t\t<td align=\"center\"><input type=\"submit\" value=\"" + buttonLabel + "\" class=\"input--main__style input--button\"></td>";
            umAnimacao += "\t\t</tr>";
            umAnimacao += "\t</table>";
            umAnimacao += "\t</form>";
        } else if (tipo == FORM_DETAIL) {
            umAnimacao += "\t<table width=\"80%\" bgcolor=\"#f3f3f3\" align=\"center\">";
            umAnimacao += "\t\t<tr>";
            umAnimacao += "\t\t\t<td colspan=\"3\" align=\"left\"><font size=\"+2\"><b>&nbsp;&nbsp;&nbsp;Detalhar Animacao (ID " + animacao.getId() + ")</b></font></td>";
            umAnimacao += "\t\t</tr>";
            umAnimacao += "\t\t<tr>";
            umAnimacao += "\t\t\t<td colspan=\"3\" align=\"left\">&nbsp;</td>";
            umAnimacao += "\t\t</tr>";
            umAnimacao += "\t\t<tr>";
            umAnimacao += "\t\t\t<td>&nbsp;Nome: " + animacao.getNome() + "</td>";
            umAnimacao += "\t\t\t<td>Duração: " + animacao.getDuracao() + "</td>";
            umAnimacao += "\t\t\t<td>Gênero: " + animacao.getGenero() + "</td>";
            umAnimacao += "\t\t</tr>";
            umAnimacao += "\t\t<tr>";
            umAnimacao += "\t\t\t<td>&nbsp;Data de fabricação: " + animacao.getDataFabricacao().toString() + "</td>";
            umAnimacao += "\t\t\t<td>Data de lançamento: " + animacao.getDataLancamento().toString() + "</td>";
            umAnimacao += "\t\t\t<td>&nbsp;</td>";
            umAnimacao += "\t\t</tr>";
            umAnimacao += "\t</table>";
        } else {
            System.out.println("ERRO! Tipo não identificado " + tipo);
        }
        form = form.replaceFirst("<UM-FILME>", umAnimacao);

        String list = new String("<table width=\"80%\" align=\"center\" bgcolor=\"#f3f3f3\">");
        list += "\n<tr><td colspan=\"6\" align=\"left\"><font size=\"+2\"><b>&nbsp;&nbsp;&nbsp;Relação de Animacoes</b></font></td></tr>\n" +
                "\n<tr><td colspan=\"6\">&nbsp;</td></tr>\n" +
                "\n<tr>\n" +
                "\t<td><a href=\"/animacao/list/" + FORM_ORDERBY_ID + "\"><b>ID</b></a></td>\n" +
                "\t<td><a href=\"/animacao/list/" + FORM_ORDERBY_NOME + "\"><b>Nome</b></a></td>\n" +
                "\t<td><a href=\"/animacao/list/" + FORM_ORDERBY_DURACAO + "\"><b>Duração</b></a></td>\n" +
                "\t<td width=\"100\" align=\"center\"><b>Detalhar</b></td>\n" +
                "\t<td width=\"100\" align=\"center\"><b>Atualizar</b></td>\n" +
                "\t<td width=\"100\" align=\"center\"><b>Excluir</b></td>\n" +
                "</tr>\n";

        List<Animacao> animacoes;
        if (orderBy == FORM_ORDERBY_ID) {
            animacoes = animacaoDAO.getOrderByID();
        } else if (orderBy == FORM_ORDERBY_NOME) {
            animacoes = animacaoDAO.getOrderByNome();
        } else if (orderBy == FORM_ORDERBY_DURACAO) {
            animacoes = animacaoDAO.getOrderByDuracao();
        } else {
            animacoes = animacaoDAO.get();
        }

        int i = 0;
        String bgcolor = "";
        for (Animacao f : animacoes) {
            bgcolor = (i++ % 2 == 0) ? "#fff5dd" : "#dddddd";
            list += "\n<tr bgcolor=\"" + bgcolor + "\">\n" +
                    "\t<td>" + f.getId() + "</td>\n" +
                    "\t<td>" + f.getNome() + "</td>\n" +
                    "\t<td>" + f.getDuracao() + "</td>\n" +
                    "\t<td align=\"center\" valign=\"middle\"><a href=\"/animacao/" + f.getId() + "\"><img src=\"/image/detail.png\" width=\"20\" height=\"20\"/></a></td>\n" +
                    "\t<td align=\"center\" valign=\"middle\"><a href=\"/animacao/update/" + f.getId() + "\"><img src=\"/image/update.png\" width=\"20\" height=\"20\"/></a></td>\n" +
                    "\t<td align=\"center\" valign=\"middle\"><a href=\"javascript:confirmarDeleteAnimacao('" + f.getId() + "', '" + f.getNome() + "', '" + f.getDuracao() + "');\"><img src=\"/image/delete.png\" width=\"20\" height=\"20\"/></a></td>\n" +
                    "</tr>\n";
        }
        list += "</table>";
        form = form.replaceFirst("<LISTAR-FILME>", list);
    }

    public Object insert(Request request, Response response) {
        String nome = request.queryParams("nome");
        float duracao = Float.parseFloat(request.queryParams("duracao"));
        String genero = request.queryParams("genero");
        LocalDateTime dataFabricacao = LocalDateTime.parse(request.queryParams("dataFabricacao"));
        LocalDate dataLancamento = LocalDate.parse(request.queryParams("dataLancamento"));

        String resp = "";

        Animacao animacao = new Animacao(-1, nome, duracao, genero, dataFabricacao, dataLancamento);

        if (animacaoDAO.insert(animacao)) {
            resp = "Animacao (" + nome + ") inserido!";
            response.status(201);
        } else {
            resp = "Animacao (" + nome + ") não inserido!";
            response.status(404);
        }

        makeForm();
        return form.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">", "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"" + resp + "\">");
    }

    public Object get(Request request, Response response) {
        int id = Integer.parseInt(request.params(":id"));
        Animacao animacao = animacaoDAO.get(id);

        if (animacao != null) {
            response.status(200);
            makeForm(FORM_DETAIL, animacao, FORM_ORDERBY_NOME);
        } else {
            response.status(404);
            String resp = "Animacao " + id + " não encontrado.";
            makeForm();
            form.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">", "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"" + resp + "\">");
        }

        return form;
    }

    public Object getToUpdate(Request request, Response response) {
        int id = Integer.parseInt(request.params(":id"));
        Animacao animacao = animacaoDAO.get(id);

        if (animacao != null) {
            response.status(200);
            makeForm(FORM_UPDATE, animacao, FORM_ORDERBY_NOME);
        } else {
            response.status(404);
            String resp = "Animacao " + id + " não encontrado.";
            makeForm();
            form.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">", "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"" + resp + "\">");
        }

        return form;
    }

    public Object getAll(Request request, Response response) {
        int orderBy = Integer.parseInt(request.params(":orderby"));
        makeForm(orderBy);
        response.header("Content-Type", "text/html");
        response.header("Content-Encoding", "UTF-8");
        return form;
    }

    public Object update(Request request, Response response) {
        int id = Integer.parseInt(request.params(":id"));
        Animacao animacao = animacaoDAO.get(id);
        String resp = "";

        if (animacao != null) {
            animacao.setNome(request.queryParams("nome"));
            animacao.setDuracao(Float.parseFloat(request.queryParams("duracao")));
            animacao.setGenero(request.queryParams("genero"));
            animacao.setDataFabricacao(LocalDateTime.parse(request.queryParams("dataFabricacao")));
            animacao.setDataLancamento(LocalDate.parse(request.queryParams("dataLancamento")));
            animacaoDAO.update(animacao);
            response.status(200);
            resp = "Animacao (ID " + animacao.getId() + ") atualizado!";
        } else {
            response.status(404);
            resp = "Animacao (ID " + animacao.getId() + ") não encontrado!";
        }
        makeForm();
        return form.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">", "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"" + resp + "\">");
    }

    public Object delete(Request request, Response response) {
        int id = Integer.parseInt(request.params(":id"));
        Animacao animacao = animacaoDAO.get(id);
        String resp = "";

        if (animacao != null) {
            animacaoDAO.delete(id);
            response.status(200);
            resp = "Animacao (" + id + ") excluído!";
        } else {
            response.status(404);
            resp = "Animacao (" + id + ") não encontrado!";
        }
        makeForm();
        return form.replaceFirst("<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"\">", "<input type=\"hidden\" id=\"msg\" name=\"msg\" value=\"" + resp + "\">");
    }
}
