/*
 * Generated by the Jasper component of Apache Tomcat
 * Version: WildFly 33.0.1.Final (WildFly Core 25.0.1.Final) - 2.3.15.Final
 * Generated at: 2024-09-29 11:15:20 UTC
 * Note: The last modified time of this file was set to
 *       the last modified time of the source file after
 *       generation to assist with modification tracking.
 */
package org.apache.jsp;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.jsp.*;
import java.util.ArrayList;
import com.server.Row;

public final class results_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent,
                 org.apache.jasper.runtime.JspSourceImports,
                 org.apache.jasper.runtime.JspSourceDirectives {

  private static final jakarta.servlet.jsp.JspFactory _jspxFactory =
          jakarta.servlet.jsp.JspFactory.getDefaultFactory();

  private static java.util.Map<java.lang.String,java.lang.Long> _jspx_dependants;

  private static final java.util.Set<java.lang.String> _jspx_imports_packages;

  private static final java.util.Set<java.lang.String> _jspx_imports_classes;

  static {
    _jspx_imports_packages = new java.util.HashSet<>();
    _jspx_imports_packages.add("jakarta.servlet");
    _jspx_imports_packages.add("jakarta.servlet.http");
    _jspx_imports_packages.add("jakarta.servlet.jsp");
    _jspx_imports_classes = new java.util.HashSet<>();
    _jspx_imports_classes.add("com.server.Row");
    _jspx_imports_classes.add("java.util.ArrayList");
  }

  private jakarta.el.ExpressionFactory _el_expressionfactory;
  private org.apache.tomcat.InstanceManager _jsp_instancemanager;

  public java.util.Map<java.lang.String,java.lang.Long> getDependants() {
    return _jspx_dependants;
  }

  public java.util.Set<java.lang.String> getPackageImports() {
    return _jspx_imports_packages;
  }

  public java.util.Set<java.lang.String> getClassImports() {
    return _jspx_imports_classes;
  }

  public boolean getErrorOnELNotFound() {
    return false;
  }

  public void _jspInit() {
    _el_expressionfactory = _jspxFactory.getJspApplicationContext(getServletConfig().getServletContext()).getExpressionFactory();
    _jsp_instancemanager = org.apache.jasper.runtime.InstanceManagerFactory.getInstanceManager(getServletConfig());
  }

  public void _jspDestroy() {
  }

  public void _jspService(final jakarta.servlet.http.HttpServletRequest request, final jakarta.servlet.http.HttpServletResponse response)
        throws java.io.IOException, jakarta.servlet.ServletException {

final java.lang.String _jspx_method = request.getMethod();
if (!"GET".equals(_jspx_method) && !"POST".equals(_jspx_method) && !"HEAD".equals(_jspx_method) && !jakarta.servlet.DispatcherType.ERROR.equals(request.getDispatcherType())) {
response.sendError(jakarta.servlet.http.HttpServletResponse.SC_METHOD_NOT_ALLOWED, "JBWEB004248: JSPs only permit GET POST or HEAD");
return;
}

    final jakarta.servlet.jsp.PageContext pageContext;
    jakarta.servlet.http.HttpSession session = null;
    final jakarta.servlet.ServletContext application;
    final jakarta.servlet.ServletConfig config;
    jakarta.servlet.jsp.JspWriter out = null;
    final java.lang.Object page = this;
    jakarta.servlet.jsp.JspWriter _jspx_out = null;
    jakarta.servlet.jsp.PageContext _jspx_page_context = null;


    try {
      response.setContentType("text/html; charset=UTF-8");
      response.addHeader("X-Powered-By", "JSP/3.1");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;

      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("<!DOCTYPE html>\r\n");
      out.write("<html lang=\"ru-RU\">\r\n");
      out.write("\r\n");
      out.write("<head>\r\n");
      out.write("    <meta charset=\"UTF-8\">\r\n");
      out.write("    <title>Лабораторная работа №2</title>\r\n");
      out.write("    <script src=\"static/js/jquery-3.7.1.min.js\"></script>\r\n");
      out.write("    <link rel=\"stylesheet\" href=\"static/css/index.css\">\r\n");
      out.write("    <link rel=\"icon\" type=\"image/jpg\" href=\"static/media/favicon.ico\">\r\n");
      out.write("</head>\r\n");
      out.write("\r\n");
      out.write("<body>\r\n");
      out.write("    <div class=\"content-container\">\r\n");
      out.write("        <header class=\"header\">\r\n");
      out.write("            <div class=\"header-container\">\r\n");
      out.write("                <div>Пышкин Никита Сергеевич P3213</div>\r\n");
      out.write("                <div></div>\r\n");
      out.write("                <div>409429</div>\r\n");
      out.write("            </div>\r\n");
      out.write("        </header>\r\n");
      out.write("        <main class=\"main\">\r\n");
      out.write("            <div class=\"main__left-column\">\r\n");
      out.write("                <div class=\"main__block\">\r\n");
      out.write("                    <a class=\"link-to-form\" href=\"/server/index\">Вернуться к форме</a>\r\n");
      out.write("                </div>\r\n");
      out.write("            </div>\r\n");
      out.write("            <div>\r\n");
      out.write("                <div class=\"result-title\">Результат</div>\r\n");
      out.write("                <div class=\"result-container\">\r\n");
      out.write("                    <div style=\"border-top: 1px solid #000000;\">X</div>\r\n");
      out.write("                    <div style=\"border-top: 1px solid #000000;\">Y</div>\r\n");
      out.write("                    <div style=\"border-top: 1px solid #000000;\">R</div>\r\n");
      out.write("                    <div style=\"border-top: 1px solid #000000;\">Попал?</div>\r\n");
      out.write("                </div>\r\n");
      out.write("                <div class=\"result-container\" id=\"result\">\r\n");
      out.write("                    ");
 Object param = request.getAttribute("new_row"); 
      out.write("\r\n");
      out.write("                    ");
 if (param != null) { 
      out.write("\r\n");
      out.write("                        ");
 Row newRow = (Row) param; 
      out.write("\r\n");
      out.write("                        <div>");
      out.print( newRow.getX() );
      out.write("</div>\r\n");
      out.write("                        <div>");
      out.print( newRow.getY() );
      out.write("</div>\r\n");
      out.write("                        <div>");
      out.print( newRow.getR() );
      out.write("</div>\r\n");
      out.write("                        <div>");
      out.print( newRow.getResult() ? "<span style='color:green'>Да</span>" : "<span style='color:red'>Нет</span>" );
      out.write("</div>\r\n");
      out.write("                    ");
 } 
      out.write("\r\n");
      out.write("                </div>\r\n");
      out.write("            </div>\r\n");
      out.write("        </main>\r\n");
      out.write("    </div>\r\n");
      out.write("</body>\r\n");
      out.write("\r\n");
      out.write("</html>\r\n");
    } catch (java.lang.Throwable t) {
      if (!(t instanceof jakarta.servlet.jsp.SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          try {
            if (response.isCommitted()) {
              out.flush();
            } else {
              out.clearBuffer();
            }
          } catch (java.io.IOException e) {}
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
        else throw new jakarta.servlet.ServletException(t);
      }
    } finally {
      _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }
}