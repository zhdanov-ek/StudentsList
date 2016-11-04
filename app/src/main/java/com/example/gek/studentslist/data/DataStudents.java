/**
 * Класс, который содержит в себе и возвращает данные о студентах
 */

package com.example.gek.studentslist.data;


import java.util.ArrayList;


public class DataStudents {
    private ArrayList<Student>  list;
    public DataStudents(){
        list = new ArrayList<>();
        list.add(new Student("Ruslan Migal", "https://github.com/rmigal", "https://plus.google.com/u/0/106331812587299981536"));
        list.add(new Student("Евгений Жданов", "https://github.com/zhdanov-ek", "https://plus.google.com/u/0/113264746064942658029"));
        list.add(new Student("Edgar Khimich", "https://github.com/lyfm", "https://plus.google.com/u/0/102197104589432395674"));
        list.add(new Student("Alexander Storchak", "https://github.com/new15", "https://plus.google.com/u/0/106553086375805780685"));
        list.add(new Student("Yevhenii Sytnyk", "https://github.com/YevheniiSytnyk", "https://plus.google.com/u/0/101427598085441575303"));
        list.add(new Student("Alyona Prelestnaya", "https://github.com/HelenCool", "https://plus.google.com/u/0/107382407687723634701"));
        list.add(new Student("Богдан Рибак", "https://github.com/BogdanRybak1996", "https://plus.google.com/u/0/103145064185261665176"));
        list.add(new Student("Ірина Смалько", "https://github.com/IraSmalko", "https://plus.google.com/u/0/113994208318508685327"));
        list.add(new Student("Владислав Винник", "https://github.com/vlads0n", "https://plus.google.com/u/0/117765348335292685488"));
        list.add(new Student("Ігор Пахаренко", "https://github.com/IhorPakharenko", "https://plus.google.com/u/0/108231952557339738781"));
        list.add(new Student("Андрей Рябко", "https://github.com/RyabkoAndrew", "https://plus.google.com/u/0/110288437168771810002"));
        list.add(new Student("Ivan Leshchenko", "https://github.com/ivleshch", "https://plus.google.com/u/0/111088051831122657934"));
        list.add(new Student("Микола Піхманець", "https://github.com/NikPikhmanets", "https://plus.google.com/u/0/110087894894730430086"));
        list.add(new Student("Руслан Воловик", "https://github.com/RuslanVolovyk", "https://plus.google.com/u/0/109719711261293841416"));
        list.add(new Student("Valerii Gubskyi", "https://github.com/gvv-ua", "https://plus.google.com/u/0/107910188078571144657"));
        list.add(new Student("Иван Сергеенко", "https://github.com/dogfight81", "https://plus.google.com/u/0/111389859649705526831"));
        list.add(new Student("Вова Лымарь", "https://github.com/VovanNec", "https://plus.google.com/u/0/109227554979939957830"));
        list.add(new Student("Даша Кириченко", "https://github.com/dashakdsr", "https://plus.google.com/u/0/103130382244571139113"));
        list.add(new Student("Michael Tyoply", "https://github.com/RedGeekPanda", "https://plus.google.com/u/0/110313151428733681846"));
        list.add(new Student("Павел Сакуров", "https://github.com/sakurov/Sunshine", "https://plus.google.com/u/0/108482088578879737406"));
    }

    public ArrayList<Student> getListStudents(){
        return list;
    }
}
