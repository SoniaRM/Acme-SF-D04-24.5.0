
package acme.features.client.dashboard;

import java.util.Collection;

import acme.client.data.datatypes.Money;

public class MoneyOperations {

	public static Money getAvg(final Collection<Money> collection, final String systemCurrency) {
		Money result;
		result = new Money();
		result.setCurrency(systemCurrency);
		result.setAmount(collection.stream().mapToDouble(Money::getAmount).average().getAsDouble());
		return result;
	}

	public static Money getStd(final Collection<Money> collection, final String systemCurrency) {
		Money result;
		result = new Money();
		result.setCurrency(systemCurrency);
		Integer n = collection.size();
		Double media = collection.stream().mapToDouble(Money::getAmount).average().getAsDouble();
		Double varianza = collection.stream().mapToDouble(Money::getAmount).map(x -> Math.pow(x - media, 2) / n).sum();
		result.setAmount(Math.sqrt(varianza));
		return result;
	}

	public static Money getMin(final Collection<Money> collection, final String systemCurrency) {
		Money result;
		result = new Money();
		result.setCurrency(systemCurrency);
		result.setAmount(collection.stream().mapToDouble(Money::getAmount).min().orElse(0.));
		return result;
	}

	public static Money getMax(final Collection<Money> collection, final String systemCurrency) {
		Money result;
		result = new Money();
		result.setCurrency(systemCurrency);
		result.setAmount(collection.stream().mapToDouble(Money::getAmount).max().orElse(0.));
		return result;
	}

}
