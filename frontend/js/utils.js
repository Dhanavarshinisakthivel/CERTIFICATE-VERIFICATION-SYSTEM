// ================================================
// utils.js - Shared helper functions
// ================================================

// Show a toast notification
function showToast(message, type = 'success') {
  let container = document.querySelector('.toast-container');
  if (!container) {
    container = document.createElement('div');
    container.className = 'toast-container';
    document.body.appendChild(container);
  }
  const toast = document.createElement('div');
  toast.className = `toast toast-${type}`;
  const icon = type === 'success' ? '✅' : '❌';
  toast.innerHTML = `<span>${icon}</span><span>${message}</span>`;
  container.appendChild(toast);
  setTimeout(() => { toast.style.opacity = '0'; toast.style.transition = 'opacity .4s'; setTimeout(() => toast.remove(), 400); }, 4000);
}

// Format a date string nicely
function formatDate(dateStr) {
  if (!dateStr) return '-';
  return new Date(dateStr).toLocaleDateString('en-IN', { year: 'numeric', month: 'long', day: 'numeric' });
}

// Copy text to clipboard
function copyToClipboard(text) {
  navigator.clipboard.writeText(text).then(() => showToast('Copied to clipboard!'));
}

// Truncate a hash for display
function shortHash(hash) {
  if (!hash || hash.length < 16) return hash;
  return hash.slice(0, 8) + '...' + hash.slice(-8);
}

// Show/hide loading spinner inside a button
function setLoading(btn, loading, originalText) {
  if (loading) {
    btn.disabled = true;
    btn.dataset.original = btn.innerHTML;
    btn.innerHTML = '<span class="spinner" style="width:18px;height:18px;border-width:2px;display:inline-block"></span>';
  } else {
    btn.disabled = false;
    btn.innerHTML = originalText || btn.dataset.original || 'Submit';
  }
}

// Get query param from URL
function getQueryParam(name) {
  return new URLSearchParams(window.location.search).get(name);
}

// Render the blockchain chain visualization
function renderBlockchain(blocks) {
  if (!blocks || blocks.length === 0) return '<p style="color:var(--muted)">No blocks yet</p>';
  return blocks.slice(-5).map((b, i) => `
    <div style="display:flex;align-items:center;gap:8px;">
      ${i > 0 ? '<div style="font-size:1.5rem;color:var(--cyan)">⛓</div>' : ''}
      <div class="card" style="flex:1;padding:14px;">
        <div style="font-size:.75rem;color:var(--muted);font-weight:700">BLOCK #${b.blockIndex ?? i}</div>
        <div class="hash-text" style="margin-top:6px;font-size:.7rem;">${b.blockHash ?? 'N/A'}</div>
      </div>
    </div>
  `).join('');
}

// Generate PDF-style certificate (uses browser print)
function downloadCertificatePDF(cert) {
  const w = window.open('', '_blank');
  w.document.write(`
    <html><head><title>Certificate - ${cert.certificateId}</title>
    <style>
      body { font-family: Georgia, serif; margin: 0; background: #fff; }
      .cert { width: 900px; margin: 40px auto; padding: 60px; border: 12px double #b8860b;
              border-radius: 16px; text-align: center; background: linear-gradient(135deg, #fffdf0, #fff8e1); }
      h1 { font-size: 2.5rem; color: #b8860b; margin-bottom: 4px; }
      .subtitle { font-size: 1.1rem; color: #666; margin-bottom: 40px; }
      .student { font-size: 2.2rem; font-weight: bold; color: #1a1a2e; margin: 20px 0; border-bottom: 2px solid #b8860b; padding-bottom: 16px; }
      .detail { font-size: 1.1rem; color: #333; margin: 8px 0; }
      .cert-id { font-family: monospace; font-size: .85rem; color: #666; margin-top: 30px; }
      .footer { margin-top: 50px; display: flex; justify-content: space-around; }
      .sig { text-align: center; }
      .sig-line { width: 200px; border-top: 2px solid #333; margin: 0 auto 6px; }
    </style></head>
    <body>
      <div class="cert">
        <h1>🏛 Certificate of Completion</h1>
        <p class="subtitle">This is to certify that</p>
        <div class="student">${cert.studentName}</div>
        <p class="detail">has successfully completed the course of</p>
        <p class="detail" style="font-size:1.4rem;font-weight:bold;color:#1a1a2e;margin:12px 0">${cert.courseName}</p>
        <p class="detail">from <strong>${cert.instituteName}</strong></p>
        <p class="detail">with grade <strong>${cert.grade}</strong></p>
        <p class="detail">on <strong>${formatDate(cert.issueDate)}</strong></p>
        <div class="cert-id">Certificate ID: ${cert.certificateId} | Blockchain Verified ✓</div>
        <div class="footer">
          <div class="sig"><div class="sig-line"></div><p>Authorized Signatory</p></div>
          <div class="sig"><div class="sig-line"></div><p>Institute Seal</p></div>
        </div>
      </div>
    </body></html>
  `);
  w.document.close();
  setTimeout(() => w.print(), 500);
}
