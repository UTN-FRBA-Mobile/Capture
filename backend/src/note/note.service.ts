import { Injectable } from '@nestjs/common';
import { InjectModel } from '@nestjs/sequelize';
import { Note } from './note.model';
import { NewNoteDto } from './dto/newNote.dto';

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
}
