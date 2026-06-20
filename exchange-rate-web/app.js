// 환율 변환 — Frankfurter API 사용 (무료, 키 불필요)
const API = "https://api.frankfurter.app";
const CURRENCIES = ["USD", "KRW", "EUR", "JPY", "GBP", "CNY"];

const $ = (id) => document.getElementById(id);

function fillSelect(sel, def) {
  sel.innerHTML = CURRENCIES.map(c => `<option ${c === def ? "selected" : ""}>${c}</option>`).join("");
}

async function convert() {
  const amount = parseFloat($("amount").value || "0");
  const from = $("from").value, to = $("to").value;
  $("result").textContent = "조회 중…";
  if (from === to) { $("result").textContent = `${amount} ${to}`; $("meta").textContent = ""; return; }
  try {
    const res = await fetch(`${API}/latest?amount=${amount}&from=${from}&to=${to}`);
    if (!res.ok) throw new Error("응답 오류");
    const data = await res.json();
    const value = data.rates[to];
    $("result").textContent = `${value.toLocaleString()} ${to}`;
    $("meta").textContent = `기준일 ${data.date} · 1 ${from} = ${(value / amount).toFixed(4)} ${to}`;
  } catch (e) {
    $("result").textContent = "환율을 불러오지 못했습니다";
    $("meta").textContent = String(e.message);
  }
}

fillSelect($("from"), "USD");
fillSelect($("to"), "KRW");
$("convert").addEventListener("click", convert);
convert();
