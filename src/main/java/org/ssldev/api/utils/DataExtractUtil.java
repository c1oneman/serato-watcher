package org.ssldev.api.utils;

import static java.util.stream.Collectors.toList;

import java.util.List;

import org.ssldev.api.chunks.Adat;
import org.ssldev.api.chunks.Oent;
import org.ssldev.api.chunks.Otrk;
import org.ssldev.api.chunks.Vrsn;
import org.ssldev.api.consumption.SslByteConsumer;

/**
 * extracts data from various chunks
 */
public class DataExtractUtil {

	/**
	 * retrieves all {@link Adat} contained by the given top level {@link SslByteConsumer}
	 * @param sessionBytes to extract from
	 * @return list of contained {@link Adat}
	 */
	public static List<Adat> extractAdat(SslByteConsumer sessionBytes) {
		/*
		 * currently session structure is as follows:
		 * [1]ssl
		 * -->[*] oent
		 * 			-->[*] adat
		 * 						--> [*] field 
		 */
		return sessionBytes.getData().stream()
					       .filter(Oent.class::isInstance).map(Oent.class::cast)
					       .map(Oent::getData).flatMap(List::stream)
					       .filter(Adat.class::isInstance).map(Adat.class::cast)
					       .collect(toList());
	}
	
	/**
	 * retrieves all {@link Otrk} contained by the given top level OTrk {@link SslByteConsumer}
	 * @param sessionBytes to extract from
	 * @return list of contained {@link Otrk}
	 */
	public static List<Otrk> extractOtrk(SslByteConsumer sessionBytes) {
		/*
		 * currently database structure is as follows:
		 * [1]ssl
		 * -->[*] Otrk
		 * 			-->[*] field
		 */
		return sessionBytes.getData().stream()
				.filter(Otrk.class::isInstance).map(Otrk.class::cast)
				.collect(toList());
	}
	
	/**
	 * retrieves all {@link Adat} contained by the given top level {@link SslByteConsumer}
	 * @param sessionBytes to extract from
	 * @return {@link Vrsn} or null if not found.
	 */
	public static Vrsn extractVrsn(SslByteConsumer sessionBytes) {
		/*
		 * currently structure is as follows:
		 * [1]ssl
		 * -->[1] vrsn
		 */
		return (Vrsn) sessionBytes.getData().stream()
				.filter(Vrsn.class::isInstance)
				.findFirst().orElse(null);
	}
}
