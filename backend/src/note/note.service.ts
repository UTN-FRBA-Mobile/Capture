import { Injectable } from '@nestjs/common';
import { InjectModel } from '@nestjs/sequelize';
import { Note } from './note.model';
import { NewNoteDto } from './dto/newNote.dto';
import { UpdateNoteDto } from './dto/updateNote.dto';

@Injectable()
export class NoteService {
  constructor(
    @InjectModel(Note)
    private noteModel: typeof Note,
  ) {}

  async create(username: string, newNoteDto: NewNoteDto) {
    return this.noteModel.create({ ...newNoteDto, username });
  }

  async fetchAllForUser(username: string) {
    return this.noteModel.findAll({
      where: {
        username,
      },
    });
  }

  async delete(id: string | number) {
    await this.noteModel.destroy({ where: { id } });
  }

  async update(dto: UpdateNoteDto, id: string | number) {
    return this.noteModel.update(dto, { where: { id } });
  }
}
