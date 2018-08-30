# Producer_Consumer

 ProducerConsumer class has two nested static classes, Producer and Consumer
 While Producer fill in a stringbuilder with current time, Consumer clean
 up the stringbuilder.
 
 This concurrent program use synchrinized block as monitor lock to lock the 
 StringBuilder, Producer will wait to process until the Consumer cleans up the
 StringBuilder, and Consumer witll wait to process until the Producer fill
 in the StringBuilder.
 
