const BASE = "http://localhost:8080";

async function apiFetch(path, options = {}) {
    const res = await fetch(BASE + path, { credentials: "include", ...options });
    if (!res.ok) throw new Error(`API error: ${res.status}`);
    return res.json();
}

let _currentUser = null;

async function checkAuth(avatarId = "userAvatar") {
    try {
        const user = await apiFetch("/api/auth/me");
        _currentUser = user;
        const el = document.getElementById(avatarId);
        if (el && user.username) el.textContent = user.username[0].toUpperCase();
        return user;
    } catch {
        location.href = "login.html";
    }
}

function getUser()  { return _currentUser; }
function isAdmin()  { return _currentUser?.role === "ROLE_ADMIN"; }
function isUser()   { return _currentUser?.role === "ROLE_USER"; }

function guardAdmin() {
    if (!isAdmin()) { location.href = "dashboard.html"; }
}

async function logout() {
    if (!confirm("Are you sure you want to logout?")) return;
    await fetch(`${BASE}/api/auth/logout`, { method: "POST", credentials: "include" });
    location.href = "login.html";
}

function fmt(n) {
    return Number(n).toLocaleString("tr-TR", { minimumFractionDigits: 2, maximumFractionDigits: 2 });
}

function setStatus(id, msg, type) {
    const el = document.getElementById(id);
    if (!el) return;
    el.textContent = msg;
    el.className   = "status-msg" + (type ? " " + type : "");
}

function renderTxList(txs, containerId, showAccount = false) {
    const el = document.getElementById(containerId);
    if (!txs.length) { el.innerHTML = '<div class="empty">No transactions yet.</div>'; return; }
    el.innerHTML = txs.map(tx => {
        const dep     = tx.type.toLowerCase() === "deposit";
        const cls     = dep ? "deposit" : "withdraw";
        const icon    = dep ? "⬆️" : "⬇️";
        const sign    = dep ? "+" : "−";
        const dateStr = tx.createdAt ? new Date(tx.createdAt).toLocaleString("tr-TR") : "–";
        const sub     = showAccount && tx.accountName ? `${dateStr} · ${tx.accountName}` : dateStr;
        return `<div class="tx-item">
      <div class="tx-icon ${cls}">${icon}</div>
      <div class="tx-det">
        <div class="tx-type">${tx.type}</div>
        <div class="tx-date">${sub}</div>
      </div>
      <div class="tx-amt ${cls}">${sign}₺${fmt(tx.amount)}</div>
    </div>`;
    }).join("");
}

function buildSidebar(activePage) {
    const user    = getUser();
    const admin   = isAdmin();

    const links = [
        { page: "dashboard",     href: "dashboard.html",     icon: "🏠", label: "Dashboard",    show: true  },
        { page: "accounts",      href: "accounts.html",      icon: "👤", label: "Accounts",     show: admin },
        { page: "transactions",  href: "transactions.html",  icon: "📄", label: "Transactions", show: true  },
        { page: "new-account",   href: "new-account.html",   icon: "➕", label: "New Account",  show: admin },
    ];

    const items = links
        .filter(l => l.show)
        .map(l => `
      <button class="nav-item ${l.page === activePage ? "active" : ""}"
              onclick="location.href='${l.href}'">
        <span class="icon">${l.icon}</span> ${l.label}
      </button>`)
        .join("");

    const roleTag = admin
        ? `<div class="role-badge admin-badge">ADMIN</div>`
        : `<div class="role-badge user-badge">USER</div>`;

    return `
    <div class="logo">🏦 <span>BankApp</span></div>
    ${items}
    <div class="sidebar-bottom">
      <div class="sidebar-user">
        <div class="sidebar-avatar">${user?.username?.[0]?.toUpperCase() ?? "?"}</div>
        <div>
          <div class="sidebar-username">${user?.username ?? ""}</div>
          ${roleTag}
        </div>
      </div>
      <button class="nav-item nav-logout" onclick="logout()">
        <span class="icon">🚪</span> Logout
      </button>
    </div>`;
}