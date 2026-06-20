import { API } from "./currencies.js";

/** Frankfurter API로 환율 변환. 실패 시 예외를 던집니다. */
export async function convert(amount, from, to) {
  if (from === to) return { value: amount, rate: 1, date: "-" };
  const res = await fetch(`${API}/latest?amount=${amount}&from=${from}&to=${to}`);
  if (!res.ok) throw new Error("환율 API 응답 오류");
  const data = await res.json();
  const value = data.rates[to];
  return { value, rate: value / amount, date: data.date };
}
