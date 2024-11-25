package org.ssldev.api.chunks;

import java.util.List;
import java.util.Optional;

import org.ssldev.api.consumption.ByteConsumer;
import org.ssldev.api.consumption.ByteConsumerIF;
import org.ssldev.api.consumption.strategies.CompoundChunkConsumeStrategy;
/**
 * serato crate track pointer wrapper
 */
public class Otrk extends ByteConsumer {

	public Otrk() {
		super();
		id = name = "otrk";
		
		consume = new CompoundChunkConsumeStrategy(this);
		constructOtrk();
	}

	private void constructOtrk() {
		register(new Ptrk());

		register(new Ttyp());
		register(new Pfil());
		register(new Tsng());
		register(new Tart());
		register(new Tgen());
		register(new Tlen());
		register(new Tbit());
		register(new Tsmp());
		register(new Talb());
		register(new Tcom());
		register(new Tcmp());
		register(new Tadd());
		register(new Tkey());
		register(new Uadd());
		register(new Utkn());
		register(new Ulbl());
		register(new Tlbl());
		register(new Utme());
		register(new Utpc());
		register(new Sbav());
		register(new Bhrt());
		register(new Bmis());
		register(new Bply());
		register(new Blop());
		register(new Bitu());
		register(new Bovc());
		register(new Bcrt());
		register(new Biro());
		register(new Bwlb());
		register(new Bwll());
		register(new Buns());
		register(new Bbgl());
		register(new Bkrk());
		register(new Tbpm());
		register(new Tgrp());
		register(new Ttyr());
		register(new Ufsb());
		register(new Tsiz());
		register(new Udsc());
		register(new Trmx());
		register(new Tcor());
		register(new Pvid());
	}

	@Override
	public ByteConsumer getInstance() {
		return new Otrk();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<ByteConsumerIF> getData() {
		return (List<ByteConsumerIF>) data;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("["+id+"]:");
		sb.append("\n");
		for(ByteConsumerIF b : getData()) {
			sb.append("\t").append(b).append("\n");
		}
		return sb.toString();
	}

	public <T> Optional<T> get(Class<T> type) {

		return getData().stream()
				.filter(type::isInstance)
				.map(type::cast)
				.findFirst();
	}
	
	public void set(Class<? extends ByteConsumerIF> type, Object val) {
		
		get(type).ifPresent(b -> b.setData(val));
	}

}
