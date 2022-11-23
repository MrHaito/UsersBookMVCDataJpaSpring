delete
from books;
delete
from persons;

alter sequence books_book_id_seq restart with 1;
alter sequence persons_person_id_seq restart with 1;

insert into persons (name, year_of_birth)
VALUES ('Tom', 1989),
       ('Mike', 1996),
       ('Katy', 2001);

insert into books (person_id, title, author, year)
VALUES (1, 'Властелин колец', 'Джордж Толкиен', 2004),
       (1, 'Ведьмак', 'Анджей Сапковский', 2019),
       (2, 'Java для начинающих', 'Кэти Сьера', 2016),
       (3, 'Атака Титанов', 'Хадзимэ Исаяма', 2010);