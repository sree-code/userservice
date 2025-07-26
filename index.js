const functions = require('firebase-functions');
const { spawn } = require('child_process');
const path = require('path');

// Cloud Function to run the Spring Boot application
exports.userservice = functions
  .runWith({
    memory: '1GB',
    timeoutSeconds: 540
  })
  .https
  .onRequest((req, res) => {
    // Set CORS headers
    res.set('Access-Control-Allow-Origin', '*');
    res.set('Access-Control-Allow-Methods', 'GET, POST, PUT, DELETE, OPTIONS');
    res.set('Access-Control-Allow-Headers', 'Content-Type, Authorization');
    
    if (req.method === 'OPTIONS') {
      res.status(204).send('');
      return;
    }

    // Forward request to Spring Boot application
    const jarPath = path.join(__dirname, 'target', 'userService-0.0.1-SNAPSHOT.jar');
    
    const springBootApp = spawn('java', [
      '-jar',
      '-Dserver.port=8080',
      '-Dspring.profiles.active=prod',
      jarPath
    ]);

    springBootApp.stdout.on('data', (data) => {
      console.log(`Spring Boot: ${data}`);
    });

    springBootApp.stderr.on('data', (data) => {
      console.error(`Spring Boot Error: ${data}`);
    });

    // Proxy the request
    const options = {
      hostname: 'localhost',
      port: 8080,
      path: req.url,
      method: req.method,
      headers: req.headers
    };

    const http = require('http');
    const proxyReq = http.request(options, (proxyRes) => {
      res.status(proxyRes.statusCode);
      Object.keys(proxyRes.headers).forEach(key => {
        res.set(key, proxyRes.headers[key]);
      });
      proxyRes.pipe(res);
    });

    proxyReq.on('error', (err) => {
      console.error('Proxy error:', err);
      res.status(500).send('Internal Server Error');
    });

    if (req.method === 'POST' || req.method === 'PUT') {
      req.pipe(proxyReq);
    } else {
      proxyReq.end();
    }
  });
