import { BadRequestException, Injectable } from '@nestjs/common';
import { InjectModel } from '@nestjs/sequelize';
import { Note } from './note.model';
import { NewNoteDto } from './dto/newNote.dto';
import { UpdateNoteDto } from './dto/updateNote.dto';
import { TagService } from '../tag/tag.service';

@Injectable()
export class NoteService {
  constructor(
    @InjectModel(Note)
    private noteModel: typeof Note,
    private readonly tagService: TagService,
  ) {}

  async create(username: string, newNoteDto: NewNoteDto) {
    const { tags, ...noteData } = newNoteDto;

    const note = await this.noteModel.create({
      ...noteData,
      username,
    });

    if (tags && tags.length > 0) {
      const tagsObjs = await this.tagService.createOrFind(tags);
      note.tags = tagsObjs;
      note.$set('tags', tagsObjs);
    }

    note.save();

    return note;
  }

  async fetchAllForUser(username: string) {
    return this.noteModel.findAll({
      where: {
        username,
      },
      include: 'tags',
    });
  }

  async delete(id: string | number) {
    await this.noteModel.destroy({ where: { id } });
  }

  async updateasdsad(dto: UpdateNoteDto, id: string | number) {
    await this.noteModel.update(dto, { where: { id } });
    return this.noteModel.findByPk(id);
  }

  async update(dto: UpdateNoteDto, id: string | number) {
    const { tags } = dto;

    const note = await this.noteModel.findByPk(id, { include: 'tags' });

    if (!note) {
      throw new BadRequestException();
    }

    note.title = dto.title;
    note.content = dto.content;

    if (!tags) {
      note.tags = [];
    } else {
      const tagsObjs = await this.tagService.createOrFind(tags);
      note.tags = tagsObjs;
      note.$set('tags', tagsObjs);
    }

    note.save();

    return note;
  }
}
