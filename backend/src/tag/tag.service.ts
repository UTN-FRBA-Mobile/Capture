import { Injectable } from '@nestjs/common';
import { InjectModel } from '@nestjs/sequelize';
import { Tag } from './tag.model';

@Injectable()
export class TagService {
  constructor(@InjectModel(Tag) private tagModel: typeof Tag) {}

  async createOrFind(tags: string[], username: string) {
    const promises = tags.map(async (t) => {
      // const o = await this.tagModel.findByPk(t);
      const o = await this.tagModel.findOne({
        where: {
          name: t,
          username,
        },
      });

      if (o !== null) {
        return o;
      }

      return await this.tagModel.create({ name: t, username });
    });

    return Promise.all(promises);
  }

  async clearTagsWithNoNotesForUser(username: string) {
    const tags = await this.tagModel.findAll({
      where: { username },
      include: 'notes',
    });

    const promises = tags.map(async (t) => {
      if (t.notes.length === 0) {
        await t.destroy();
      } else {
        return Promise.resolve();
      }
    });

    await Promise.all(promises);
  }

  async updateColours(username: string, tags: Map<string, string>) {
    const promises = Object.entries(tags).map(async ([noteName, colour]) => {
      const note = await this.tagModel.findOne({
        where: {
          name: noteName,
          username,
        },
      });
      note.colour = colour;
      await note.save();
    });

    await Promise.all(promises);
  }
}
