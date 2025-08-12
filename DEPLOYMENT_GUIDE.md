# 🚀 RPPL Training Server - Deployment Guide

## 📋 **Overview**
This guide helps you deploy the RPPL Training Server to a company with limited IT resources. The solution is packaged as a self-contained JAR executable that requires minimal technical knowledge to install and run.

## 🎯 **What You'll Get**
- **Single JAR file** containing everything needed
- **Simple startup scripts** for Windows and Linux/Mac
- **No complex installation** required
- **Self-contained** - no external dependencies to manage

## 📦 **Deployment Package Contents**
```
RPPL-Training-Deployment/
├── training-server.jar          # Main executable
├── start-training-server.bat    # Windows startup script
├── start-training-server.sh     # Linux/Mac startup script
├── Training/                    # Training materials folder
│   ├── RPPL Profile 24-25 new.pdf
│   ├── HR.pdf
│   ├── Fundamentals.pdf
│   ├── Glue.pdf
│   ├── Health & Safety Training.pdf
│   ├── PPE_Training.pdf
│   ├── Training - Question paper - Document control.docx
│   ├── Training - Question paper - Internal Audit.docx
│   └── Training - Question paper - NC management.docx
├── logo.jpg                     # Company logo
├── config.properties            # Configuration file
├── DEPLOYMENT_GUIDE.md          # This guide
└── README.md                    # User manual
```

## 🔧 **Prerequisites**
- **Java 11 or later** installed on the target machine
- **Port 8000** available (can be changed if needed)

## 📥 **Installation Steps**

### **Step 1: Download and Extract**
1. Download the deployment package
2. Extract to a folder (e.g., `C:\RPPL-Training` or `/opt/rppl-training`)
3. Ensure all files are in the same folder

### **Step 2: Install Java (if not already installed)**
- **Windows**: Download from https://adoptium.net/ and run installer
- **Linux**: `sudo apt install openjdk-11-jre` (Ubuntu/Debian)
- **Mac**: Download from https://adoptium.net/ and run installer

### **Step 3: Start the Server**
- **Windows**: Double-click `start-training-server.bat`
- **Linux/Mac**: 
  ```bash
  chmod +x start-training-server.sh
  ./start-training-server.sh
  ```

### **Step 4: Access the Training Dashboard**
- Open any web browser
- Navigate to: `http://localhost:8000`
- The training dashboard will load automatically

## 🎮 **Usage Instructions**

### **Starting the Server**
1. **Double-click** the startup script for your operating system
2. **Wait** for the "Server started successfully" message
3. **Keep the window open** while using the training system

### **Stopping the Server**
1. **Press Ctrl+C** in the server window
2. **Wait** for "Server stopped" message
3. **Close** the window

### **Accessing Training Materials**
1. **Open your web browser**
2. **Go to** `http://localhost:8000`
3. **Click** on any training module to view content
4. **Use** the expandable "Company Policies & Procedures" module for policy documents

## ⚙️ **Configuration Options**

### **Changing the Port**
If port 8000 is already in use, you can change it:
1. **Edit** `config.properties`
2. **Change** `server.port=8000` to `server.port=8080` (or any available port)
3. **Restart** the server
4. **Access** via `http://localhost:8080`

### **Adding New Training Materials**
1. **Place** new PDF/DOCX files in the `Training/` folder
2. **Update** `config.properties` if needed
3. **Restart** the server

## 🚨 **Troubleshooting**

### **"Java is not recognized" Error**
- **Solution**: Install Java 11+ from https://adoptium.net/
- **Verify**: Open command prompt and type `java -version`

### **"Port already in use" Error**
- **Solution**: Change port in `config.properties` or stop other services using port 8000
- **Alternative**: Use `netstat -ano | findstr :8000` to find what's using the port

### **"File not found" Errors**
- **Solution**: Ensure all files are in the correct folders
- **Check**: Training materials are in `Training/` folder, logo in root folder

### **Server won't start**
- **Check**: Java version (must be 11 or later)
- **Verify**: All files are present and in correct locations
- **Try**: Running `java -jar training-server.jar` directly for error messages

## 🔒 **Security Considerations**
- **Local access only** - server runs on localhost
- **No external connections** - completely internal
- **File-based access** - training materials stored locally
- **No user authentication** - suitable for internal company use

## 📱 **Network Deployment (Optional)**
To make the server accessible to other computers on the network:
1. **Find** the server computer's IP address
2. **Change** `config.properties` to use `0.0.0.0` instead of `localhost`
3. **Access** from other computers via `http://[SERVER-IP]:8000`

## 📞 **Support**
For technical support or questions:
- **Check** this deployment guide first
- **Review** error messages in the server window
- **Ensure** Java 11+ is installed
- **Verify** all files are in correct locations

## 🎉 **Success Indicators**
- Server shows "Training server started successfully at http://localhost:8000/"
- Web browser can access `http://localhost:8000`
- Training modules load and display content
- PDF documents open in the browser
- Expandable policy module works correctly

---
**Deployment completed successfully when users can access the training dashboard and view all training materials!** 🚀
