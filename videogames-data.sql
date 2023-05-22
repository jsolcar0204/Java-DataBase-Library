INSERT INTO desarrollador VALUES
    ('Naughty Dog', '1984-08-27'),
    ('Santa Monica Studios', '1999-01-01'),
    ('Guerrilla Games', '2000-01-01'),
    ('Kinetic Games', '2015-01-01'),
    ('Capcom', '1979-05-30'),
    ('Mojang', '2009-05-01');

INSERT INTO editor VALUES
    ('PlayStation', '1994-12-03'),
    ('Kinetic Games', '2015-01-01'),
    ('Capcom', '1979-05-30'),
    ('Mojang', '2009-05-01');

INSERT INTO genero VALUES
    ('Accion', 'Contiene muchos momentos de alta actividad'),
    ('Terror', 'Contiene miedo o sustos'),
    ('Aventura', 'Contiene exploración y buena vista'),
    ('Buena trama', 'Contiene una historia bien desarrollada'),
    ('Mitologia', 'Contiene historia o el juego basado en mitología'),
    ('Combate', 'Contiene peleas tanto cuerpo a cuerpo como con armas');

INSERT INTO plataforma VALUES
    ('PC'),
    ('Nintendo Switch'),
    ('PlayStation 3'),
    ('PlayStation 4'),
    ('PlayStation 5'),
    ('Xbox 360'),
    ('Xbox One'),
    ('Xbox Series X');

INSERT INTO videojuego VALUES
    ('The Last of Us', 'En una civilización asolada, plagada de infectados y crueles supervivientes, Joel, nuestro exhausto protagonista, es contratado para sacar a escondidas a una chica de 14 años, Ellie, de una zona militar en cuarentena. Pero lo que comienza siendo una simple tarea, pronto se transforma en un brutal viaje por el país.', '2023-03-28', 'Naughty Dog', 'PlayStation'),
    ('God of War', 'Kratos ha dejado atrás su venganza contra los dioses del Olimpo y vive ahora como un hombre en los dominios de los dioses y monstruos nórdicos. En este mundo cruel e implacable debe luchar para sobrevivir… y enseñar a su hijo a hacerlo también.', '2022-01-14', 'Santa Monica Studios', 'PlayStation'),
    ('Horizon Zero Dawn', 'LA TIERRA YA NO NOS PERTENECE. Vive la misión legendaria de Aloy para desvelar los secretos de un mundo dominado por máquinas letales. La joven cazadora, una paria, lucha por revelar su pasado, descubrir su destino y detener una amenaza catastrófica para el futuro. Desata devastadores ataques contra máquinas únicas y tribus rivales mientras exploras un mundo abierto repleto de vida salvaje y peligros.', '2020-08-07', 'Guerrilla Games', 'PlayStation'),
    ('Phasmophobia', 'Phasmophobia is a 4-player, online co-op, psychological horror game. You and your team of paranormal investigators will enter haunted locations filled with paranormal activity and try to gather as much evidence as you can. Use your ghost-hunting equipment to find and record evidence to sell on to a ghost removal team.', '2020-09-18', 'Kinetic Games', 'Kinetic Games'),
    ('Resident Evil 4 Remake', 'Sobrevivir es solo el principio. Seis años después de la catástrofe biológica en Raccoon City, el agente Leon S. Kennedy, uno de los supervivientes del desastre, ha sido enviado a rescatar a la hija del presidente, a quien han secuestrado. Siguiendo su rastro, llega a una apartada población europea, donde sus habitantes sufren un mal terrible. Así comienza esta historia de un arriesgado rescate y terror escalofriante donde se cruzan la vida y la muerte, y el miedo y la catarsis. Con una mecánica de juego modernizada, una historia reimaginada y unos gráficos espectacularmente detallados, Resident Evil 4 supone el renacimiento de un gigante del mundo de los videojuegos. Revive la pesadilla que revolucionó el género del survival horror.', '2023-03-24', 'Capcom', 'Capcom'),
    ('Minecraft', 'Minecraft es un videojuego de mundo abierto donde la exploración y las construcciones son fundamentales. Creado por Markus <<Notch>> Persson, nos permite desarrollar nuestros propios universos fantásticos y artísticos, mediante la colocación y destrucción de bloques.', '2011-11-18', 'Mojang', 'Mojang');

INSERT INTO videojuego_genero VALUES
    ('The Last of Us', 'Accion'),
    ('The Last of Us', 'Terror'),
    ('God of War', 'Combate'),
    ('God of War', 'Aventura'),
    ('God of War', 'Mitologia'),
    ('God of War', 'Buena trama'),
    ('God of War', 'Accion'),
    ('Horizon Zero Dawn', 'Combate'),
    ('Horizon Zero Dawn', 'Aventura'),
    ('Horizon Zero Dawn', 'Accion'),
    ('Phasmophobia', 'Terror'),
    ('Phasmophobia', 'Accion'),
    ('Minecraft', 'Aventura'),
    ('Resident Evil 4 Remake', 'Terror'),
    ('Resident Evil 4 Remake', 'Accion');
INSERT INTO videojuego_plataforma VALUES
    ('The Last of Us', 'PC'),
    ('The Last of Us', 'PlayStation 3'),
    ('The Last of Us', 'PlayStation 4'),
    ('God of War', 'PC'),
    ('God of War', 'PlayStation 4'),
    ('Phasmophobia', 'PC'),
    ('Minecraft', 'PC'),
    ('Minecraft', 'PlayStation 3'),
    ('Minecraft', 'PlayStation 4'),
    ('Minecraft', 'Xbox 360'),
    ('Minecraft', 'Xbox One'),
    ('Minecraft', 'Xbox Series X');