����   2 � 2kotlinx/coroutines/internal/ConcurrentLinkedListKt  java/lang/Object  ConcurrentLinkedList.kt POINTERS_SHIFT I    CLOSED $Lkotlinx/coroutines/internal/Symbol; findSegmentInternal Z(Lkotlinx/coroutines/internal/Segment;JLkotlin/jvm/functions/Function2;)Ljava/lang/Object; �<S:Lkotlinx/coroutines/internal/Segment<TS;>;>(TS;JLkotlin/jvm/functions/Function2<-Ljava/lang/Long;-TS;+TS;>;)Ljava/lang/Object;     #kotlinx/coroutines/internal/Segment  getId ()J  
   
getRemoved ()Z  
   4kotlinx/coroutines/internal/ConcurrentLinkedListNode  access$getNextOrClosed$p J(Lkotlinx/coroutines/internal/ConcurrentLinkedListNode;)Ljava/lang/Object;  
   access$getCLOSED$p &()Lkotlinx/coroutines/internal/Symbol;   
  ! +kotlinx/coroutines/internal/SegmentOrClosed # constructor-impl &(Ljava/lang/Object;)Ljava/lang/Object; % &
 $ ' java/lang/Long ) valueOf (J)Ljava/lang/Long; + ,
 * - kotlin/jvm/functions/Function2 / invoke 8(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object; 1 2 0 3 
trySetNext 9(Lkotlinx/coroutines/internal/ConcurrentLinkedListNode;)Z 5 6
  7 remove ()V 9 :
  ; F$i$a$-nextOrIfClosed-ConcurrentLinkedListKt$findSegmentInternal$next$1 it$iv Ljava/lang/Object; 6$i$a$-let-ConcurrentLinkedListNode$nextOrIfClosed$1$iv this_$iv 6Lkotlinx/coroutines/internal/ConcurrentLinkedListNode; $i$f$nextOrIfClosed newTail %Lkotlinx/coroutines/internal/Segment; next cur $this$findSegmentInternal id J createNewSegment  Lkotlin/jvm/functions/Function2; $i$f$findSegmentInternal close n(Lkotlinx/coroutines/internal/ConcurrentLinkedListNode;)Lkotlinx/coroutines/internal/ConcurrentLinkedListNode; G<N:Lkotlinx/coroutines/internal/ConcurrentLinkedListNode<TN;>;>(TN;)TN; #Lorg/jetbrains/annotations/NotNull; markAsClosed R 
  S 8$i$a$-nextOrIfClosed-ConcurrentLinkedListKt$close$next$1 $this$close getCLOSED$annotations 	 
	  X access$findSegmentInternal  
  [ #$this$access_u24findSegmentInternal Lkotlin/Metadata; mv       bv    k    d1,��8



��
	
��








#8��"��*8��0��*8��H��¢o8��0"��*8��0*8��202840¢	
(8��¢	
(8��0Hø��¢"08@X¢
"08@XT¢

¨ d2 N S   Lkotlin/Function2; Lkotlin/ParameterName; name prev -Lkotlinx/coroutines/internal/SegmentOrClosed; kotlinx-coroutines-core <clinit> "kotlinx/coroutines/internal/Symbol s 	 <init> (Ljava/lang/String;)V v w
 t x ConstantValue Code StackMapTable LineNumberTable LocalVariableTable 	Signature RuntimeInvisibleAnnotations $RuntimeInvisibleParameterAnnotations 
Deprecated 
SourceFile SourceDebugExtension RuntimeVisibleAnnotations 1          z      	 
        {       �6*:� �� � � �� :6� :	6
6	:6� "� 6� "� (��    � :� 
:���-� 
a� .� 4 � :� � 8� � � � <:��p� (�    |   � �  � /   0       �    0       � 0   0       �    0    }   V        # � 7 � ? � B  I � N � O � P � Q  V  [  _  b  y   � ! � " �  � % ~   z  B  =   4  > ?  7  @     1 A B  # . C   y  D E  V A F E   � G E    � H E     � I J    � K L   � M         N O  {  :     C*L +N6-� :66:6	� "� 6
+��    M,� +� T� +�,L���    |   A �  � % 
        �  
         }   F    Q  R  S   # & S ( - . /  0 S 1 T 5 U > W @ X @ R ~   R  &  U  
   > ?    @  	  + A B   ( C   1  F B   A G ?    C V B       P �     Q   �     Q  
 W :  {          �     �         {         � Y�    }        Z   {   E     *-� \�    }        ~         ] E      I J     K L   r :  {   %      � tYu� y� Y�    }       �  �     �  ISMAP
ConcurrentLinkedList.kt
Kotlin
*S Kotlin
*F
+ 1 ConcurrentLinkedList.kt
kotlinx/coroutines/internal/ConcurrentLinkedListKt
+ 2 ConcurrentLinkedList.kt
kotlinx/coroutines/internal/ConcurrentLinkedListNode
+ 3 AtomicFU.common.kt
kotlinx/atomicfu/AtomicFU_commonKt
*L
1#1,240:1
24#1,3:250
27#1,11:260
44#1:271
45#1,8:274
105#2,7:241
105#2,7:253
105#2,7:282
92#3,2:248
92#3,2:272
*E
*S KotlinDebug
*F
+ 1 ConcurrentLinkedList.kt
kotlinx/coroutines/internal/ConcurrentLinkedListKt
*L
71#1,3:250
71#1,11:260
72#1:271
72#1,8:274
26#1,7:241
71#1,7:253
83#1,7:282
44#1,2:248
72#1,2:272
*E
 �     ^  _[ I `I aI  b[ I `I I c dI e f[ s g h[ s Bs is Ns Os Es js ks Is ls ms ns os Ks ps s s 
s 	s 
s Ws :s ks s s q                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             