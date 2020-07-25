package net.rolodophone.paraula.world

val levels = listOf(
	Level(196, 42, listOf(
		Phrase("hello",				"hola",				setOf(Example("hello, I am $", "hola, soc en $"))),
		Phrase("I am",				"soc",				setOf(Example("hello, I am $", "hola, soc en $")))
	)),
	Level(210, 105, listOf()),
	Level(208, 173, listOf())
)