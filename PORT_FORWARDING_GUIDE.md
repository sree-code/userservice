# Port Forwarding Setup for Internet Access

## ⚠️ WARNING: Security Considerations
Port forwarding exposes your application to the internet. Only do this for testing, not production!

## 📋 Steps for Port Forwarding:

### Step 1: Find Your Public IP
Visit: https://whatismyipaddress.com/
Example: Your public IP might be `203.45.67.89`

### Step 2: Configure Router Port Forwarding
1. Open router admin panel (usually http://192.168.1.1)
2. Login with admin credentials
3. Find "Port Forwarding" or "Virtual Servers"
4. Add new rule:
   - **Service Name**: Spring Boot Dev
   - **External Port**: 8080
   - **Internal IP**: 192.168.1.10 (your computer)
   - **Internal Port**: 8080
   - **Protocol**: TCP

### Step 3: Configure Windows Firewall
Run as Administrator:
```cmd
netsh advfirewall firewall add rule name="Spring Boot Public" dir=in action=allow protocol=TCP localport=8080
```

### Step 4: Test Access
From anywhere in the world:
```
http://203.45.67.89:8080/health
http://203.45.67.89:8080/api/user/getProfile/test@example.com
```

## ⚠️ Security Risks:
- Your application is exposed to the internet
- No authentication on most endpoints
- Potential for attacks
- Not recommended for production

## 🛡️ Security Improvements Needed:
1. Enable HTTPS/SSL
2. Add authentication to sensitive endpoints
3. Use VPN instead
4. Consider using ngrok for temporary testing

## 🚀 Better Alternative: Use ngrok (Temporary Solution)
```cmd
# Install ngrok
# Download from https://ngrok.com/

# Expose your local server
ngrok http 8080
```

This gives you a temporary public URL like:
`https://abc123.ngrok.io` that forwards to your local `localhost:8080`
