package control;

import model.ProdottoBean;
import model.ProdottoDAO;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

@WebServlet("/collezione")
public class Collezione extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public Collezione() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String mode = request.getParameter("mode");
        ProdottoDAO dao = new ProdottoDAO();
        List<ProdottoBean> prodotti = new ArrayList<>();
        List<ProdottoBean> randomProdotti = new ArrayList<>();

        try {
            // Recupera tutti i prodotti
            prodotti = dao.doRetrieveAll("");

            // Ottieni 3 prodotti casuali
            randomProdotti = getRandomProducts(prodotti, 3);

            // Imposta entrambe le liste nella sessione
            request.getSession().setAttribute("prodotti", prodotti);
            request.getSession().setAttribute("randomProdotti", randomProdotti);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        String targetPage = "/collezione.jsp"; // Default page

        if ("home".equalsIgnoreCase(mode)) {
            targetPage = "/home.jsp";
        }

        // Reindirizza alla pagina appropriata
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(targetPage);
        dispatcher.forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    private List<ProdottoBean> getRandomProducts(List<ProdottoBean> products, int count) {
        List<ProdottoBean> randomProducts = new ArrayList<>();
        Set<Integer> selectedIndexes = new HashSet<>();
        Random random = new Random();

        if (products.size() <= count) {
            // Se ci sono meno o esattamente il numero di prodotti richiesti, restituisci tutti i prodotti
            return new ArrayList<>(products);
        }

        while (randomProducts.size() < count) {
            int randomIndex = random.nextInt(products.size());
            if (!selectedIndexes.contains(randomIndex)) {
                randomProducts.add(products.get(randomIndex));
                selectedIndexes.add(randomIndex);
            }
        }

        return randomProducts;
    }
}