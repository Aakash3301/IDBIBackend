# Financial Health Score Algorithm

The `FinancialHealthEngine` analyzes the synced historical data to generate a comprehensive credit score out of a maximum of 1,000 points.

The core algorithm calculates averages over the synced history (currently 6 months) and assigns weights based on three primary metrics: **Revenue Growth**, **Digital Adoption**, and **Liquidity**.

## 1. Average Monthly Revenue (Max 400 Points)
This metric evaluates the absolute scale and cash generation capacity of the business based on their GST filings.
- **> ₹15,00,000 (15 Lakhs)** = 400 Points
- **> ₹5,00,000 (5 Lakhs)** = 300 Points
- **< ₹5,00,000** = 150 Points

## 2. Digital Adoption via UPI Volume (Max 300 Points)
This metric measures how digitally active the business is by looking at the average number of UPI transactions per month. High transaction volume indicates a modern retail or service approach with transparent digital trails.
- **> 1,000 transactions/month** = 300 Points
- **> 500 transactions/month** = 200 Points
- **< 500 transactions/month** = 100 Points

## 3. Liquidity Ratio (Max 300 Points)
This metric calculates the ratio between the Average Bank Balance and the Average Monthly Revenue. A higher ratio means the business keeps a healthy cash buffer relative to its turnover.
- **Balance is > 40% of Revenue** = 300 Points
- **Balance is > 20% of Revenue** = 200 Points
- **Balance is < 20% of Revenue** = 100 Points

---

## Final Assessment and Risk Tiers

The algorithm adds the points from the three metrics together to determine the final `RiskCategory`, `LoanEligibility`, and `RecommendedLoanAmount`.

### High Score (800 - 1000 Points)
- **Risk Level**: LOW
- **Loan Eligibility**: ELIGIBLE
- **Recommendation**: Eligible for a high loan amount equivalent to **3x (Three times) their Average Monthly Revenue**.

### Average Score (600 - 799 Points)
- **Risk Level**: MEDIUM
- **Loan Eligibility**: REVIEW
- **Recommendation**: Eligible for a moderate loan amount equivalent to **1x their Average Monthly Revenue**, pending manual review.

### Low Score (< 600 Points)
- **Risk Level**: HIGH
- **Loan Eligibility**: REJECTED
- **Recommendation**: ₹0. The business currently carries too much risk for automated lending.
