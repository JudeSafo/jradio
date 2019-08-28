package ru.r2cloud.jradio.lrpt;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.r2cloud.jradio.ByteInput;
import ru.r2cloud.jradio.Context;
import ru.r2cloud.jradio.MessageInput;
import ru.r2cloud.jradio.PhaseAmbiguityResolver;
import ru.r2cloud.jradio.Tag;
import ru.r2cloud.jradio.blocks.CorrelateAccessCodeTag;
import ru.r2cloud.jradio.blocks.FixedLengthTagger;
import ru.r2cloud.jradio.blocks.TaggedStreamToPdu;
import ru.r2cloud.jradio.fec.ViterbiSoft;
import ru.r2cloud.jradio.fec.ccsds.Randomize;
import ru.r2cloud.jradio.fec.ccsds.ReedSolomon;
import ru.r2cloud.jradio.fec.ccsds.UncorrectableException;

public class LRPT implements MessageInput {

	private static final Logger LOG = LoggerFactory.getLogger(LRPT.class);
	private final ViterbiSoft viterbiSoft;
	private final PhaseAmbiguityResolver phaseAmbiguityResolver;
	private final MessageInput messageInput;

	public LRPT(ByteInput input) {
		this.phaseAmbiguityResolver = new PhaseAmbiguityResolver(0x035d49c24ff2686bL);
		this.viterbiSoft = new ViterbiSoft((byte) 0x4f, (byte) 0x6d, false, VCDU.VITERBI_TAIL_SIZE);
		CorrelateAccessCodeTag correlate = new CorrelateAccessCodeTag(input, 17, phaseAmbiguityResolver.getSynchronizationMarkers(), true);
		FixedLengthTagger tagger = new FixedLengthTagger(correlate, VCDU.VITERBI_TAIL_SIZE);
		messageInput = new TaggedStreamToPdu(tagger);
	}

	@Override
	public byte[] readBytes() throws IOException {
		while (true) {
			byte[] raw = messageInput.readBytes();
			try {
				return decode(raw);
			} catch (UncorrectableException e) {
				if (LOG.isDebugEnabled()) {
					LOG.debug("unable to decode: {}", e.getMessage());
				}
			}
		}
	}

	private byte[] decode(byte[] rawBytes) throws UncorrectableException {
		Tag currentTag = messageInput.getContext().getCurrent();
		phaseAmbiguityResolver.rotateSoft(rawBytes, (Long) currentTag.get(CorrelateAccessCodeTag.ACCESS_CODE));
		byte[] viterbi = viterbiSoft.decode(rawBytes);
		Randomize.shuffle(viterbi);
		return ReedSolomon.decode(viterbi, 4);
	}

	@Override
	public Context getContext() {
		return messageInput.getContext();
	}

	@Override
	public void close() throws IOException {
		messageInput.close();
	}
}
