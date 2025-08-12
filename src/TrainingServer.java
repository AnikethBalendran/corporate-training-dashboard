import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.util.logging.Logger;
import java.util.logging.Level;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;

public class TrainingServer {
    private static final Logger LOGGER = Logger.getLogger(TrainingServer.class.getName());
    private static final int DEFAULT_PORT = 8000;
    
    // Enhanced CSS with modern design system
    private static final String COMMON_STYLE = """
        <style>
            :root {
                --primary-color: #059669;
                --primary-dark: #047857;
                --secondary-color: #6b7280;
                --accent-color: #fbbf24;
                --success-color: #10b981;
                --danger-color: #ef4444;
                --background-color: #f0fdf4;
                --surface-color: #ffffff;
                --text-primary: #064e3b;
                --text-secondary: #6b7280;
                --border-color: #d1fae5;
                --shadow-sm: 0 1px 2px 0 rgb(0 0 0 / 0.05);
                --shadow-md: 0 4px 6px -1px rgb(0 0 0 / 0.1), 0 2px 4px -2px rgb(0 0 0 / 0.1);
                --shadow-lg: 0 10px 15px -3px rgb(0 0 0 / 0.1), 0 4px 6px -4px rgb(0 0 0 / 0.1);
                --shadow-xl: 0 20px 25px -5px rgb(0 0 0 / 0.1), 0 8px 10px -6px rgb(0 0 0 / 0.1);
            }
            
            * {
                box-sizing: border-box;
            }
            
            body {
                font-family: 'Inter', -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
                margin: 0;
                padding: 0;
                background: linear-gradient(135deg, #10b981 0%, #059669 50%, #047857 100%);
                min-height: 100vh;
                color: var(--text-primary);
                line-height: 1.6;
            }
            
            .container {
                max-width: 1200px;
                margin: 0 auto;
                padding: 0 1rem;
            }
            
            header {
                background: rgba(255, 255, 255, 0.95);
                backdrop-filter: blur(10px);
                border-bottom: 1px solid var(--border-color);
                padding: 1.5rem 0;
                position: sticky;
                top: 0;
                z-index: 100;
                box-shadow: var(--shadow-sm);
            }
            
            .header-content {
                display: flex;
                align-items: center;
                justify-content: space-between;
            }
            
            .logo-section {
                display: flex;
                align-items: center;
                gap: 1rem;
            }
            
            .logo-section img {
                width: 48px;
                height: 48px;
                border-radius: 12px;
                box-shadow: var(--shadow-md);
            }
            
            .header-title {
                font-size: 1.75rem;
                font-weight: 700;
                color: var(--primary-color);
                margin: 0;
            }
            
            .header-subtitle {
                font-size: 0.875rem;
                color: var(--text-secondary);
                margin: 0;
            }
            
            main {
                padding: 3rem 0;
            }
            
            .welcome-section {
                text-align: center;
                margin-bottom: 3rem;
                color: white;
            }
            
            .welcome-title {
                font-size: 2.5rem;
                font-weight: 800;
                margin-bottom: 1rem;
                text-shadow: 0 2px 4px rgba(0,0,0,0.3);
            }
            
            .welcome-subtitle {
                font-size: 1.125rem;
                opacity: 0.9;
                max-width: 600px;
                margin: 0 auto;
            }
            
            .module-grid {
                display: grid;
                grid-template-columns: repeat(auto-fit, minmax(320px, 1fr));
                gap: 2rem;
                margin-bottom: 3rem;
            }
            
            .module-card {
                background: var(--surface-color);
                border-radius: 16px;
                padding: 2rem;
                box-shadow: var(--shadow-lg);
                transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
                border: 1px solid var(--border-color);
                position: relative;
                overflow: hidden;
            }
            
            .module-card::before {
                content: '';
                position: absolute;
                top: 0;
                left: 0;
                right: 0;
                height: 4px;
                background: linear-gradient(90deg, var(--primary-color), var(--success-color));
            }
            
            .module-card:hover {
                transform: translateY(-8px);
                box-shadow: var(--shadow-xl);
                border-color: var(--primary-color);
            }
            
            .module-icon {
                width: 48px;
                height: 48px;
                background: linear-gradient(135deg, var(--primary-color), var(--primary-dark));
                border-radius: 12px;
                display: flex;
                align-items: center;
                justify-content: center;
                margin-bottom: 1.5rem;
                color: white;
                font-size: 1.5rem;
                font-weight: 600;
            }
            
            .module-title {
                font-size: 1.25rem;
                font-weight: 700;
                color: var(--text-primary);
                margin: 0 0 1rem 0;
                line-height: 1.3;
            }
            
            .module-description {
                color: var(--text-secondary);
                margin-bottom: 1.5rem;
                font-size: 0.95rem;
                line-height: 1.6;
            }
            
            .module-actions {
                display: flex;
                flex-direction: column;
                gap: 0.75rem;
            }
            
            .btn {
                padding: 0.75rem 1.5rem;
                border: none;
                border-radius: 8px;
                font-size: 0.875rem;
                font-weight: 600;
                cursor: pointer;
                transition: all 0.2s ease;
                text-decoration: none;
                display: inline-flex;
                align-items: center;
                justify-content: center;
                gap: 0.5rem;
            }
            
            .btn-primary {
                background: var(--primary-color);
                color: white;
            }
            
            .btn-primary:hover {
                background: var(--primary-dark);
                transform: translateY(-1px);
                box-shadow: var(--shadow-md);
            }
            
            .btn-secondary {
                background: var(--surface-color);
                color: var(--primary-color);
                border: 2px solid var(--primary-color);
            }
            
            .btn-secondary:hover {
                background: var(--primary-color);
                color: white;
                transform: translateY(-1px);
            }
            
            .btn-success {
                background: var(--success-color);
                color: white;
            }
            
            .btn-success:hover {
                background: #059669;
                transform: translateY(-1px);
                box-shadow: var(--shadow-md);
            }
            
            .content-viewer {
                background: var(--surface-color);
                border-radius: 16px;
                padding: 2rem;
                box-shadow: var(--shadow-lg);
                margin-bottom: 2rem;
            }
            
            .content-header {
                display: flex;
                align-items: center;
                justify-content: space-between;
                margin-bottom: 2rem;
                padding-bottom: 1rem;
                border-bottom: 2px solid var(--border-color);
            }
            
            .content-title {
                font-size: 1.75rem;
                font-weight: 700;
                color: var(--text-primary);
                margin: 0;
            }
            
            .content-iframe {
                width: 100%;
                height: 70vh;
                border: none;
                border-radius: 12px;
                box-shadow: var(--shadow-md);
            }
            
            .navigation-bar {
                position: fixed;
                bottom: 0;
                left: 0;
                right: 0;
                background: rgba(255, 255, 255, 0.95);
                backdrop-filter: blur(10px);
                border-top: 1px solid var(--border-color);
                padding: 1rem 0;
                box-shadow: 0 -4px 6px -1px rgba(0, 0, 0, 0.1);
            }
            
            .nav-buttons {
                display: flex;
                justify-content: space-between;
                align-items: center;
                max-width: 1200px;
                margin: 0 auto;
                padding: 0 1rem;
            }
            
            .nav-group {
                display: flex;
                gap: 1rem;
            }
            
            .progress-indicator {
                display: flex;
                align-items: center;
                gap: 0.5rem;
                color: var(--text-secondary);
                font-size: 0.875rem;
            }
            
            .progress-bar {
                width: 200px;
                height: 6px;
                background: var(--border-color);
                border-radius: 3px;
                overflow: hidden;
            }
            
            .progress-fill {
                height: 100%;
                background: linear-gradient(90deg, var(--primary-color), var(--success-color));
                border-radius: 3px;
                transition: width 0.3s ease;
            }
            
            @media (max-width: 768px) {
                .container {
                    padding: 0 0.75rem;
                }
                
                .welcome-title {
                    font-size: 2rem;
                }
                
                .module-grid {
                    grid-template-columns: 1fr;
                    gap: 1.5rem;
                }
                
                .nav-buttons {
                    flex-direction: column;
                    gap: 1rem;
                }
                
                .nav-group {
                    width: 100%;
                    justify-content: center;
                }
                
                .progress-indicator {
                    display: none;
                }
            }
            
            .loading {
                display: inline-block;
                width: 20px;
                height: 20px;
                border: 3px solid rgba(255,255,255,.3);
                border-radius: 50%;
                border-top-color: #fff;
                animation: spin 1s ease-in-out infinite;
            }
            
            @keyframes spin {
                to { transform: rotate(360deg); }
            }
            
            /* Expandable Module Styles */
            .expandable-module {
                cursor: pointer;
            }
            
            .expandable-module .module-actions button {
                pointer-events: none;
            }
            
            .sub-modules {
                margin-top: 1.5rem;
                border-top: 1px solid var(--border-color);
                padding-top: 1.5rem;
            }
            
            .sub-module {
                background: var(--background-color);
                border: 1px solid var(--border-color);
                border-radius: 8px;
                padding: 1rem;
                margin-bottom: 0.75rem;
                cursor: pointer;
                transition: all 0.2s ease;
                display: flex;
                align-items: center;
                gap: 1rem;
            }
            
            .sub-module:last-child {
                margin-bottom: 0;
            }
            
            .sub-module:hover {
                background: var(--surface-color);
                border-color: var(--primary-color);
                transform: translateX(4px);
                box-shadow: var(--shadow-sm);
            }
            
            .sub-icon {
                width: 32px;
                height: 32px;
                background: linear-gradient(135deg, var(--primary-color), var(--primary-dark));
                border-radius: 6px;
                display: flex;
                align-items: center;
                justify-content: center;
                color: white;
                font-size: 1rem;
                flex-shrink: 0;
            }
            
            .sub-content {
                flex: 1;
            }
            
            .sub-content h4 {
                margin: 0 0 0.25rem 0;
                font-size: 1rem;
                font-weight: 600;
                color: var(--text-primary);
            }
            
            .sub-content p {
                margin: 0;
                font-size: 0.875rem;
                color: var(--text-secondary);
                line-height: 1.4;
            }
            
            /* Animation for expandable modules */
            .sub-modules {
                animation: slideDown 0.3s ease-out;
            }
            
            @keyframes slideDown {
                from {
                    opacity: 0;
                    transform: translateY(-10px);
                }
                to {
                    opacity: 1;
                    transform: translateY(0);
                }
            }
        </style>
    """;

    private static void serveHtml(HttpExchange exchange, String title, String iframeSrc,
            String nextPage, String quizPage, int currentStep, int totalSteps) throws IOException {
        
        int progressPercentage = (currentStep * 100) / totalSteps;
        
        String html = String.format("""
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>%s - Training Dashboard</title>
                <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700;800&display=swap" rel="stylesheet">
                %s
            </head>
            <body>
                <header>
                    <div class="container">
                        <div class="header-content">
                            <div class="logo-section">
                                <img src="logo.jpg" alt="Company Logo">
                                <div>
                                    <h1 class="header-title">Training Dashboard</h1>
                                    <p class="header-subtitle">Professional Development Portal</p>
                                </div>
                            </div>
                        </div>
                    </div>
                </header>
                
                <main>
                    <div class="container">
                        <div class="content-viewer">
                            <div class="content-header">
                                <h2 class="content-title">%s</h2>
                            </div>
                            <iframe src="%s" class="content-iframe" title="Training Content"></iframe>
                        </div>
                    </div>
                </main>
                
                <div class="navigation-bar">
                    <div class="nav-buttons">
                        <div class="nav-group">
                            <button onclick="window.history.back()" class="btn btn-secondary">
                                ← Back
                            </button>
                        </div>
                        
                        <div class="progress-indicator">
                            <span>Progress: %d/%d</span>
                            <div class="progress-bar">
                                <div class="progress-fill" style="width: %d%%"></div>
                            </div>
                        </div>
                        
                        <div class="nav-group">
                            <button onclick="window.location.href='%s'" class="btn btn-primary">
                                Next →
                            </button>
                            <button onclick="window.location.href='%s'" class="btn btn-success">
                                Take Quiz
                            </button>
                        </div>
                    </div>
                </div>
            </body>
            </html>
            """, title, COMMON_STYLE, title, iframeSrc, currentStep, totalSteps, progressPercentage, nextPage, quizPage);

        sendResponse(exchange, html, "text/html; charset=UTF-8");
    }

    private static void sendResponse(HttpExchange exchange, String content, String contentType) throws IOException {
        byte[] response = content.getBytes();
        exchange.getResponseHeaders().set("Content-Type", contentType);
        exchange.sendResponseHeaders(200, response.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response);
        }
    }

    private static void sendFileResponse(HttpExchange exchange, File file, String mimeType) throws IOException {
        try {
            byte[] fileData = Files.readAllBytes(file.toPath());
            exchange.getResponseHeaders().set("Content-Type", mimeType);
            exchange.sendResponseHeaders(200, fileData.length);
            exchange.getResponseBody().write(fileData);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error reading file: " + file.getPath(), e);
            String errorResponse = "Error reading file: " + e.getMessage();
            exchange.sendResponseHeaders(500, errorResponse.length());
            exchange.getResponseBody().write(errorResponse.getBytes());
        } finally {
            exchange.getResponseBody().close();
        }
    }

    public static void main(String[] args) throws IOException {
        int port = DEFAULT_PORT;
        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                LOGGER.warning("Invalid port number, using default: " + DEFAULT_PORT);
            }
        }

        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

        // Home page with enhanced UI
        server.createContext("/", exchange -> {
            String html = """
                <!DOCTYPE html>
                <html lang="en">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <title>Training Dashboard - Welcome</title>
                    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700;800&display=swap" rel="stylesheet">
                    %s
                </head>
                <body>
                    <header>
                        <div class="container">
                            <div class="header-content">
                                <div class="logo-section">
                                    <img src="logo.jpg" alt="Company Logo">
                                    <div>
                                        <h1 class="header-title">Training Dashboard</h1>
                                        <p class="header-subtitle">Professional Development Portal</p>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </header>
                    
                    <main>
                        <div class="container">
                            <div class="welcome-section">
                                <h1 class="welcome-title">Welcome to Your Training Journey</h1>
                                <p class="welcome-subtitle">
                                    Access comprehensive training materials and assessments designed to enhance your professional skills and knowledge.
                                </p>
                            </div>
                            
                                                         <div class="module-grid">
                                 <div class="module-card">
                                     <div class="module-icon">🏢</div>
                                     <h3 class="module-title">About Us - RPPL Profile</h3>
                                     <p class="module-description">
                                         Learn about our company, mission, values, and organizational structure. Discover what makes RPPL unique and our commitment to excellence.
                                     </p>
                                     <div class="module-actions">
                                         <a href="about.html" class="btn btn-primary">View Profile</a>
                                         <a href="quiz_about.html" class="btn btn-success">Take Quiz</a>
                                     </div>
                                 </div>
                                
                                <div class="module-card">
                                    <div class="module-icon">👥</div>
                                    <h3 class="module-title">HR Guidelines & Procedures</h3>
                                    <p class="module-description">
                                        Human Resources guidelines, employee policies, and workplace procedures for all team members.
                                    </p>
                                    <div class="module-actions">
                                        <a href="HR.html" class="btn btn-primary">View Module</a>
                                        <a href="quiz_hr.html" class="btn btn-success">Take Quiz</a>
                                    </div>
                                </div>
                                
                                <div class="module-card">
                                    <div class="module-icon">📦</div>
                                    <h3 class="module-title">Corrugated Box Fundamentals</h3>
                                    <p class="module-description">
                                        Core principles, manufacturing processes, and best practices in corrugated box production.
                                    </p>
                                    <div class="module-actions">
                                        <a href="Fundamentals.html" class="btn btn-primary">View Module</a>
                                        <a href="quiz_fundamentals.html" class="btn btn-success">Take Quiz</a>
                                    </div>
                                </div>
                                
                                <div class="module-card">
                                    <div class="module-icon">🔒</div>
                                    <h3 class="module-title">Adhesive Safety & Handling</h3>
                                    <p class="module-description">
                                        Safe handling procedures, usage guidelines, and safety protocols for industrial adhesives.
                                    </p>
                                    <div class="module-actions">
                                        <a href="Glue.html" class="btn btn-primary">View Module</a>
                                        <a href="quiz_glue.html" class="btn btn-success">Take Quiz</a>
                                    </div>
                                </div>
                                
                                                                 <div class="module-card expandable-module" onclick="toggleSubModules(this)">
                                     <div class="module-icon">📋</div>
                                     <h3 class="module-title">Company Policies & Procedures</h3>
                                     <p class="module-description">
                                         Access essential company policies, procedures, and guidelines that govern our workplace operations and standards.
                                     </p>
                                     <div class="module-actions">
                                         <button class="btn btn-primary">View Policies</button>
                                         <button class="btn btn-success">Take Assessment</button>
                                     </div>
                                     
                                     <div class="sub-modules" style="display: none;">
                                         <div class="sub-module" onclick="loadSubModule('/Training/Training - Question paper - Document control.docx', 'Document Control Policy')">
                                             <div class="sub-icon">📄</div>
                                             <div class="sub-content">
                                                 <h4>Document Control Policy</h4>
                                                 <p>Company policy and procedures for document control, version management, and information governance.</p>
                                             </div>
                                         </div>
                                         
                                         <div class="sub-module" onclick="loadSubModule('/Training/Training - Question paper - Internal Audit.docx', 'Internal Audit Policy')">
                                             <div class="sub-icon">🔍</div>
                                             <div class="sub-content">
                                                 <h4>Internal Audit Policy</h4>
                                                 <p>Company policy and procedures for internal audit processes, compliance monitoring, and quality assurance.</p>
                                             </div>
                                         </div>
                                         
                                         <div class="sub-module" onclick="loadSubModule('/Training/Training - Question paper - NC management.docx', 'Non-Conformity Management Policy')">
                                             <div class="sub-icon">⚠️</div>
                                             <div class="sub-content">
                                                 <h4>Non-Conformity Management Policy</h4>
                                                 <p>Company policy and procedures for identifying, managing, and resolving non-conformities and corrective actions.</p>
                                             </div>
                                         </div>
                                     </div>
                                 </div>
                            </div>
                                                 </div>
                     </main>
                     
                     <script>
                         function toggleSubModules(moduleCard) {
                             const subModules = moduleCard.querySelector('.sub-modules');
                             const isVisible = subModules.style.display !== 'none';
                             
                             if (isVisible) {
                                 subModules.style.display = 'none';
                             } else {
                                 subModules.style.display = 'block';
                             }
                         }
                         
                         function loadSubModule(url, title) {
                             // Create a new window/tab to display the document
                             window.open(url, '_blank');
                         }
                     </script>
                 </body>
                 </html>
                """.formatted(COMMON_STYLE);

            sendResponse(exchange, html, "text/html; charset=UTF-8");
        });

        // File serving with better error handling
        server.createContext("/Training", exchange -> {
            try {
                String path = exchange.getRequestURI().getPath();
                File file = new File("." + path);

                if (!file.exists() || file.isDirectory()) {
                    String notFound = "404 File Not Found: " + path;
                    exchange.sendResponseHeaders(404, notFound.length());
                    exchange.getResponseBody().write(notFound.getBytes());
                    exchange.getResponseBody().close();
                    return;
                }

                String mimeType = path.endsWith(".pdf") ? "application/pdf" : 
                                Files.probeContentType(file.toPath());
                if (mimeType == null) {
                    mimeType = "application/octet-stream";
                }
                
                sendFileResponse(exchange, file, mimeType);
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error serving file", e);
                String errorResponse = "Internal Server Error";
                exchange.sendResponseHeaders(500, errorResponse.length());
                exchange.getResponseBody().write(errorResponse.getBytes());
                exchange.getResponseBody().close();
            }
        });

        // Logo serving
        server.createContext("/logo.jpg", exchange -> {
            try {
                File file = new File("./logo.jpg");
                if (!file.exists()) {
                    exchange.sendResponseHeaders(404, 0);
                    exchange.close();
                    return;
                }
                sendFileResponse(exchange, file, "image/jpeg");
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error serving logo", e);
                exchange.sendResponseHeaders(500, 0);
                exchange.close();
            }
        });

                 // Training module routes with progress tracking
         server.createContext("/about.html", exchange -> 
             serveHtml(exchange, "About Us - RPPL Profile", "/Training/RPPL Profile 24-25 new.pdf", 
                      "HR.html", "quiz_about.html", 1, 5));
        
        server.createContext("/HR.html", exchange -> 
            serveHtml(exchange, "HR Guidelines & Procedures", "/Training/HR.pdf", 
                     "Fundamentals.html", "quiz_hr.html", 2, 5));
        
        server.createContext("/Fundamentals.html", exchange -> 
            serveHtml(exchange, "Corrugated Box Fundamentals", "/Training/Fundamentals.pdf", 
                     "Glue.html", "quiz_fundamentals.html", 3, 5));
        
        server.createContext("/Glue.html", exchange -> 
            serveHtml(exchange, "Adhesive Safety & Handling", "/Training/Glue.pdf", 
                     "Health.html", "quiz_glue.html", 4, 5));
        
                 // Training Resources module is now expandable and doesn't need a separate route

        server.setExecutor(null);
        server.start();
        
        LOGGER.info("Training server started successfully at http://localhost:" + port + "/");
        System.out.println("🚀 Training server started at http://localhost:" + port + "/");
        System.out.println("📚 Access the training dashboard to begin your learning journey!");
    }
}
