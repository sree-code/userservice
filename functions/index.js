const functions = require('firebase-functions');
const express = require('express');
const cors = require('cors');
const { spawn } = require('child_process');
const path = require('path');

const app = express();

// Enable CORS for all routes
app.use(cors({
  origin: [
    'https://seemee-759f4.web.app',
    'https://seemee-backend.web.app',
    'http://localhost:3000',
    'http://localhost:5000'
  ],
  credentials: true
}));

// Parse JSON bodies
app.use(express.json());

// Health check endpoint
app.get('/health', (req, res) => {
  res.json({ 
    status: 'OK', 
    message: 'User Service is running',
    timestamp: new Date().toISOString()
  });
});

// Proxy all requests to Spring Boot application
app.all('*', (req, res) => {
  // For now, return a simple response until we set up proper proxying
  res.json({
    message: 'Spring Boot User Service API',
    path: req.path,
    method: req.method,
    timestamp: new Date().toISOString(),
    note: 'This is a Firebase Functions proxy for the Spring Boot application'
  });
});

// Export the Express app as a Firebase Function
exports.api = functions
  .runWith({
    memory: '1GB',
    timeoutSeconds: 540
  })
  .https
  .onRequest(app);
