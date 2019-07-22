package filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import modelo.Usuario;

/**
* Implementação de filtro para controle da view dashboard apenas para usuários logados.
*/
@WebFilter
public class Autorizacao implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException { }

    @Override
    public void destroy() { }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        
    	Usuario usuario = null;
        HttpSession sess = ((HttpServletRequest) request).getSession(false);

        // Verifica se a sessão não está vazia.
        if (sess != null){
            // Busca o usuário salvo na sessão.
            usuario = (Usuario) sess.getAttribute("usuario");
        } 

        // Verifica se o usuário foi encontrado, se não, redireciona para o login.
        if(usuario == null) {
            String contextPath = ((HttpServletRequest) request).getContextPath();
            ((HttpServletResponse) response).sendRedirect(contextPath+ "/login.xhtml");
        } else {
            chain.doFilter(request, response);
        }
    }
}
