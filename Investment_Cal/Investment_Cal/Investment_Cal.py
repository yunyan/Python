import sys, os
import math
import time

class House():
    _construction_area = 0
    _inner_area = 0
    _price_per_square = 0

    def __init__(self, construction_area, inner_area, price_per_square):
        self._construction_area = construction_area
        self._inner_area = inner_area
        self._price_per_square = price_per_square

    def get_total_price(self, construction_area = True):
        return self._construction_area * self._price_per_square if construction_area is True else self._inner_area * self._price_per_square

class Payment():
    _house = None
    _first_payment_percentage = 0
    _yearly_interest_rate = 0
    _interest_multiplier = 0
    _loan_period = 0 # in month
    _auxiliary_expense = 0
    _refund_list = []

    def __init__(self, house, first_payment_percentage, yearly_interest_rate, interests_multiplier, auxiliary_expense, loan_period):
        self._house = house
        self._first_payment_percentage = first_payment_percentage
        self._yearly_interest_rate = yearly_interest_rate
        self._interest_multiplier = interests_multiplier
        self._auxiliary_expense = auxiliary_expense
        self._loan_period = loan_period
        
    def get_first_payment_amount(self):
        total_price = house.get_total_price(False)
        return total_price * self._first_payment_percentage

    def get_rest_payment(self):
        return  house.get_total_price(False) * (1 - self._first_payment_percentage)

    def calculate_refund_plan(self):
        loan_amount = self._house.get_total_price() * (1 - self._first_payment_percentage)        
        monthly_interest_rate = self._yearly_interest_rate * self._interest_multiplier / 12
        pow_base = math.pow((1+monthly_interest_rate), self._loan_period)
        monthly_refund = loan_amount * monthly_interest_rate * pow_base / (pow_base - 1)
        #print("total_price is {0}, loan_amount is {1}, yearly_ineterest_rate is {2}, monthly_interest_rate is {3}, monthly_refund is {4}:".format(self._total_price, loan_amount, self._yearly_interest_rate*self._interest_multiplier, monthly_interest_rate, monthly_refund))
        for i in range(0, self._loan_period):
            interest_amount = loan_amount * monthly_interest_rate
            return_capital = monthly_refund - interest_amount
            self._refund_list.append([round(return_capital, 2), round(interest_amount, 2)])
            loan_amount -= return_capital
            #print(self._refund_list[i])

    def get_refund_plan(self):
        if len(self._refund_list) == 0:
            self.calculate_refund_plan()
        return self._refund_list
         
class Investment():

    _invest_period = 0
    _payment = None
    _no_risk_profit_rate = 0

    def __init__(self, payment, invest_period, yearly_no_risk_profit_rate):
        self._invest_period = invest_period
        self._payment = payment
        self._yearly_no_risk_profit_rate = yearly_no_risk_profit_rate 

    def cal_non_risk_profit(self):
        return self._payment.get_first_payment_amount() * math.pow( (1+self._yearly_no_risk_profit_rate), self._invest_period/12 ) - self._payment.get_first_payment_amount()


    def cal_target_price(self):
        interest_paid = 0
        capital_returned = 0
        total_capital_paid = 0

        if self._payment is not None:
            for i in range(0, self._invest_period):
                capital_returned += self._payment.get_refund_plan()[i][0]
                interest_paid +=  self._payment.get_refund_plan()[i][1]
                #print("{0}: monthly_captial_return is {1}, monthly_interest is {2}, capital returned in total is {3}, interest_paid in total is {4}".format(i, self._payment.get_refund_plan()[i][0], self._payment.get_refund_plan()[i][1], capital_principle_returned, interest_paid))
            total_capital_paid = capital_returned + interest_paid + self._payment.get_first_payment_amount()
            owe_bank = self._payment.get_rest_payment() - capital_returned

        print("Total capital paid is {0}, owe_bank is {1}, non_risk_profit is {2}".format(total_capital_paid, owe_bank, self.cal_non_risk_profit()))

        return total_capital_paid + owe_bank + round(self.cal_non_risk_profit(),2)



if __name__ == '__main__':
    first_payment_percentage = 0.5
    yearly_interest_rate = 0.049
    interest_multiplier = 1.25
    auxiliary_expense = 20000
    house = House(64, 51, 12500)
    payment = Payment(house, first_payment_percentage, yearly_interest_rate, interest_multiplier, auxiliary_expense, 360)
    payment.calculate_refund_plan()
    capital_spent = house.get_total_price(False) * first_payment_percentage
    investment = Investment(payment, 60, 0.05)
    print(investment.cal_target_price())
    print(investment.cal_target_price() / house._inner_area)
    
    