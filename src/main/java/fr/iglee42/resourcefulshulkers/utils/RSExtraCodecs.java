package fr.iglee42.resourcefulshulkers.utils;

import com.mojang.datafixers.util.Function6;
import com.mojang.datafixers.util.Function8;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.network.codec.StreamCodec;

import java.util.function.Function;

public final class RSExtraCodecs {

    public static final Codec<Integer> COLOR = Codec.withAlternative(
            Codec.INT,
            Codec.STRING.flatXmap(s->{
                try {
                    String formatted = s.replace("#", "");
                    return DataResult.success(Integer.parseInt(formatted, 16));
                } catch (NumberFormatException e) {
                    return DataResult.error(()->"Invalid color format: " + s,0);
                }
            }, i-> DataResult.success(String.format("#%06X", i)))
    );

    public static <B, C, T1, T2, T3, T4, T5, T6, T7, T8> StreamCodec<B, C> composite(final StreamCodec<? super B, T1> codec1, final Function<C, T1> provider1, final StreamCodec<? super B, T2> codec2, final Function<C, T2> provider2, final StreamCodec<? super B, T3> codec3, final Function<C, T3> provider3, final StreamCodec<? super B, T4> codec4, final Function<C, T4> provider4, final StreamCodec<? super B, T5> codec5, final Function<C, T5> provider5, final StreamCodec<? super B, T6> codec6, final Function<C, T6> provider6, final StreamCodec<? super B, T7> codec7, final Function<C, T7> provider7, final StreamCodec<? super B, T8> codec8, final Function<C, T8> provider8, final Function8<T1, T2, T3, T4, T5, T6, T7, T8, C> constructor) {
        return new StreamCodec<B, C>() {
            public C decode(B buffer) {
                T1 t1 = (T1)codec1.decode(buffer);
                T2 t2 = (T2)codec2.decode(buffer);
                T3 t3 = (T3)codec3.decode(buffer);
                T4 t4 = (T4)codec4.decode(buffer);
                T5 t5 = (T5)codec5.decode(buffer);
                T6 t6 = (T6)codec6.decode(buffer);
                T7 t7 = (T7)codec7.decode(buffer);
                T8 t8 = (T8)codec8.decode(buffer);
                return (C)constructor.apply(t1, t2, t3, t4, t5, t6, t7, t8);
            }

            public void encode(B buf, C object) {
                codec1.encode(buf, provider1.apply(object));
                codec2.encode(buf, provider2.apply(object));
                codec3.encode(buf, provider3.apply(object));
                codec4.encode(buf, provider4.apply(object));
                codec5.encode(buf, provider5.apply(object));
                codec6.encode(buf, provider6.apply(object));
                codec7.encode(buf, provider7.apply(object));
                codec8.encode(buf, provider8.apply(object));
            }
        };
    }


}
