# Score Calculation Criteria

This document outlines the basis and methodology used to calculate the financial health scores for an MSME based on Account Aggregator (AA) and EPFO mock data.

## 1. Account Aggregator (AA) Score Calculation

The AA Score evaluates an MSME's bank statement data (parsed from `AA.json`) to determine its liquidity, cash flow health, and account activity. The score is calculated out of a maximum of **100 points**, broken down into three main rules:

### Rule 1: Balance Check (Up to 30 points)
Evaluates the `currentBalance` available in the MSME's bank summary. A higher balance implies better liquidity and a safety net for loan repayments.
* **> ₹1,00,000**: +30 points
* **> ₹50,000**: +20 points
* **> ₹10,000**: +10 points

### Rule 2: Cash Flow Check (Up to 40 points)
Evaluates the total incoming funds (`totalCredit`) versus the total outgoing funds (`totalDebit`) across all transactions. A positive cash flow indicates a healthy business operation.
* **Credits > Debits by at least 20%** (`totalCredit > totalDebit * 1.2`): +40 points
* **Credits > Debits** (Positive cash flow): +30 points
* **Credits > 80% of Debits** (Manageable negative cash flow): +15 points

### Rule 3: Transaction Volume (Up to 30 points)
Evaluates the level of business activity by looking at the total number of transactions. More transactions generally indicate an active, operating business.
* **> 20 transactions**: +30 points
* **> 10 transactions**: +20 points
* **> 0 transactions**: +10 points

---

## 2. EPFO Score Calculation

The EPFO Score evaluates an MSME's statutory compliance based on employee provident fund filings (parsed from `EPFO.json`). The score is calculated out of a maximum of **100 points**, broken down into two main rules:

### Rule 1: On-time Filing Rate (Up to 70 points)
Evaluates the historical percentage of EPFO filings made on or before the due date. This demonstrates the business owner's reliability and compliance behavior.
* **Calculation**: `onTimeFilingRate * 0.7`
* *Example: An 83.3% on-time filing rate contributes roughly 58 points to the final score.*

### Rule 2: Recent Filings Consistency (Up to 30 points)
Evaluates how consistently the MSME has filed returns over the last 6 months (`monthsFiledLast6`). Consistent filings suggest a stable workforce and ongoing operations.
* **6 out of 6 months filed**: +30 points
* **4 or more months filed**: +20 points
* **2 or more months filed**: +10 points
