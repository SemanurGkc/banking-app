
const API = "http://localhost:8080/api/v1/accounts"; // Backend URL

/* Navigation */
function hideAll() {
    document.getElementById("dashboard").classList.add("hidden");
    document.getElementById("accounts").classList.add("hidden");
    document.getElementById("create").classList.add("hidden");
}

function showDashboard() {
    hideAll();
    document.getElementById("dashboard").classList.remove("hidden");
    document.getElementById("pageTitle").innerText = "Dashboard";
    loadStats();
}

function showAccounts() {
    hideAll();
    document.getElementById("accounts").classList.remove("hidden");
    document.getElementById("pageTitle").innerText = "Hesaplar";
    loadAccounts();
}

function showCreate() {
    hideAll();
    document.getElementById("create").classList.remove("hidden");
    document.getElementById("pageTitle").innerText = "Yeni Hesap";
}

/* API */
async function loadAccounts() {

    const res = await fetch(API);
    const data = await res.json();

    const table = document.getElementById("accountTable");
    table.innerHTML = "";

    data.forEach(acc => {

        const row = `
      <tr>
        <td>${acc.id}</td>
        <td>${acc.accountHolderName}</td>
        <td>₺ ${acc.balance}</td>
        <td>
        <button class="btn btn-view" onclick="deposit(${acc.id})">+ Yatır</button>
        <button class="btn btn-view" onclick="withdraw(${acc.id})">- Çek</button>
        <button class="btn btn-delete" onclick="deleteAccount(${acc.id})">Sil</button>
           </td>

      </tr>
    `;

        table.innerHTML += row;
    });
}

async function createAccount() {

    const owner = document.getElementById("owner").value;
    const balance = document.getElementById("balance").value;

    const body = {
        accountHolderName: owner,
        balance: balance
    };

    await fetch(API, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(body)
    });

    alert("Hesap oluşturuldu!");

    document.getElementById("owner").value = "";
    document.getElementById("balance").value = "";

    showAccounts();
}

async function deleteAccount(id) {

    if (!confirm("Silmek istediğine emin misin?")) return;

    await fetch(API + "/" + id, {
        method: "DELETE"
    });

    loadAccounts();
}

async function loadStats() {

    const res = await fetch(API);
    const data = await res.json();

    let total = 0;

    data.forEach(acc => total += acc.balance);

    document.getElementById("totalBalance").innerText = "₺ " + total;
    document.getElementById("accountCount").innerText = data.length;
}

function logout() {
    alert("Çıkış yapıldı (Demo)");
}

async function deposit(id) {

    const amount = prompt("Yatırılacak miktar:");

    if (!amount || amount <= 0) return;

    await fetch(API + "/" + id + "/deposit", {
        method: "PUT",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            amount: amount
        })
    });

    alert("Para yatırıldı ✅");

    loadAccounts();
    loadStats();
}


async function withdraw(id) {

    const amount = prompt("Çekilecek miktar:");

    if (!amount || amount <= 0) return;

    await fetch(API + "/" + id + "/withdraw", {
        method: "PUT",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            amount: amount
        })
    });

    alert("Para çekildi ✅");

    loadAccounts();
    loadStats();
}

// Init
showDashboard();
