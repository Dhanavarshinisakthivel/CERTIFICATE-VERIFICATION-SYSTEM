// ================================================
// api.js - All backend API calls in one place
// Change API_BASE to your deployed backend URL
// ================================================

const API_BASE = 'http://localhost:8080/api';

// Get JWT token from localStorage (set after login)
function getToken() {
  return localStorage.getItem('admin_token');
}

// Standard fetch with auth header
async function apiFetch(endpoint, options = {}) {
  const token = getToken();
  const headers = {
    'Content-Type': 'application/json',
    ...(token ? { 'Authorization': `Bearer ${token}` } : {}),
    ...(options.headers || {})
  };
  const response = await fetch(`${API_BASE}${endpoint}`, { ...options, headers });
  const data = await response.json();
  return { status: response.status, data };
}

// ── Auth ──
const AuthAPI = {
  login: (username, password) =>
    apiFetch('/auth/login', {
      method: 'POST',
      body: JSON.stringify({ username, password })
    })
};

// ── Certificates ──
const CertAPI = {
  issue:      (payload) => apiFetch('/certificates/issue', { method: 'POST', body: JSON.stringify(payload) }),
  getAll:     ()        => apiFetch('/certificates'),
  getById:    (id)      => apiFetch(`/certificates/${id}`),
  search:     (name)    => apiFetch(`/certificates/search?name=${encodeURIComponent(name)}`),
  verify:     (id)      => apiFetch(`/certificates/verify/${id}`),
  revoke:     (id)      => apiFetch(`/certificates/${id}/revoke`, { method: 'PUT' }),
  stats:      ()        => apiFetch('/certificates/stats'),
};

// ── Auth helpers ──
function isLoggedIn() {
  return !!getToken();
}

function logout() {
  localStorage.removeItem('admin_token');
  localStorage.removeItem('admin_user');
  window.location.href = '/pages/login.html';
}

function requireAuth() {
  if (!isLoggedIn()) {
    window.location.href = '/pages/login.html';
    return false;
  }
  return true;
}
