import sys, os
import math
import time

class House():
    _consruction_area = 0
    _inner_area = 0
    _price_per_square = 0

    def __init__(self, construction_area, inner_area, price_per_square):
        self._consruction_area = construction_area
        self._inner_area = inner_area
        self._price_per_square = price_per_square

    def get_total_price(self, construction_area = True):
        return self._consruction_area * self._price_per_square if construction_area is True else self._inner_area * self._price_per_square

class Payment():
    _total_price = 0
    _first_payment_percentage = 0
    _yearly_interest_rate = 0
    _interest_multiplier = 0
    _loan_period = 0 # in month
    _auxiliary_expense = 0
    _refund_list = []

    def __init__(self, total_price, first_payment_percentage, yearly_interest_rate, interests_multiplier, auxiliary_expense, loan_period):
        self._total_price = total_price
        self._first_payment_percentage = first_payment_percentage
        self._yearly_interest_rate = yearly_interest_rate
        self._interest_multiplier = interests_multiplier
        self._auxiliary_expense = auxiliary_expense
        self._loan_period = loan_period
        
    def calculate_refund_plan(self):
        loan_amount = self._total_price * (1 - self._first_payment_percentage)        
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

    def __init__(self, payment, invest_period):
        self._invest_period = invest_period
        self._payment = payment

    def cal_target_price(self):
        interest_paid = 0
        capital_principle_returned = 0
        total_capital_paid = 0

        if self._payment is not None:
            for i in range(0, self._invest_period):
                capital_principle_returned += self._payment.get_refund_plan()[i][0]
                interest_paid +=  self._payment.get_refund_plan()[i][1]
                print("{0}: monthly_captial_return is {1}, monthly_interest is {2}, capital returned in total is {3}, interest_paid in total is {4}".format(i, self._payment.get_refund_plan()[i][0], self._payment.get_refund_plan()[i][1], capital_principle_returned, interest_paid))
            total_capital_paid = capital_principle_returned + interest_paid + self._payment._first_payment_percentage*self._payment._total_price
            owe_bank = self._payment._total_price*(1-self._payment._first_payment_percentage) - capital_principle_returned

        return total_capital_paid + owe_bank    



if __name__ == '__main__':
    first_payment_percentage = 0.5
    yearly_interest_rate = 0.049
    interest_multiplier = 1.25
    auxiliary_expense = 20000
    house = House(64, 51, 12500)
    payment = Payment(house.get_total_price(False), first_payment_percentage, yearly_interest_rate, interest_multiplier, auxiliary_expense, 360)
    payment.calculate_refund_plan()
    capital_spent = house.get_total_price(False) * first_payment_percentage
    investment = Investment(payment, 60)
    print(investment.cal_target_price())
    
    